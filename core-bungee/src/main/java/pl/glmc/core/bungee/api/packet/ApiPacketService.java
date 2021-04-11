package pl.glmc.core.bungee.api.packet;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.stream.MalformedJsonException;
import net.md_5.bungee.api.ChatColor;
import pl.glmc.api.common.packet.Packet;
import pl.glmc.api.common.packet.PacketInfo;
import pl.glmc.api.common.packet.listener.PacketListener;
import pl.glmc.api.common.packet.PacketService;
import pl.glmc.api.common.packet.listener.ResponseHandlerListener;
import pl.glmc.core.bungee.GlmcCoreBungee;
import pl.glmc.core.common.packets.LocalPacketRegistry;

import java.io.NotActiveException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ApiPacketService implements PacketService {
    private final GlmcCoreBungee plugin;

    private final HashMap<String, List<PacketListener<? extends Packet>>> registeredListeners;
    private final HashMap<String, Class<? extends Packet>> registeredPacketClasses;
    private final HashMap<String, PacketInfo> registeredPackets;

    private final Method registrationMethod;

    public ApiPacketService(final GlmcCoreBungee plugin) {
        this.plugin = plugin;

        this.registeredListeners = new HashMap<>();
        this.registeredPackets = new HashMap<>();
        this.registeredPacketClasses = new HashMap<>();

        try {
            this.registrationMethod = PacketListener.class.getDeclaredMethod("register", Gson.class);
            this.registrationMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            this.plugin.getLogger().warning(ChatColor.RED + "Failed to load packet service!");

            throw new RuntimeException("Failed to load registration method!");
        }


        this.registerDefaultPackets();
    }

    @Override
    public void sendPacket(final Packet packet, final String target) {
        this.defaultSendPacket(packet, target, null);
    }

    @Override
    public void sendPacket(Packet packet, String target, String additionalId) {
        this.defaultSendPacket(packet, target, additionalId);
    }

    private void defaultSendPacket(Packet packet, String target, String additionalId) {
        packet.setSender(this.plugin.getServerId());

        String jsonPacket = this.plugin.getGson().toJson(packet);
        String packetChannel = additionalId == null ? target + "." + packet.getPacketId() : target + "." + packet.getPacketId() + "." + additionalId;

        this.plugin.getLogger().info(ChatColor.YELLOW + "Sending packet " + packet.getPacketId() + " to " + target + " on channel " + packetChannel);

        //System.out.println(ChatColor.RED + "" + "Sending " + System.currentTimeMillis());
        this.plugin.getRedisProvider().publish(packetChannel, jsonPacket);
    }

    @Override
    public void registerListener(PacketListener<? extends Packet> packetListener) {
        this.defaultRegisterListener(packetListener, null);
    }

    @Override
    public void registerListener(PacketListener<? extends Packet> packetListener, String additionalId) {
        this.defaultRegisterListener(packetListener, additionalId);
    }

    private void defaultRegisterListener(PacketListener<? extends Packet> packetListener, String additionalId) {
        String channel = additionalId == null ? packetListener.getPacketInfo().getId() : packetListener.getPacketInfo().getId() + "." + additionalId;

        try {
            this.registrationMethod.invoke(packetListener, this.plugin.getGson());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();

            this.plugin.getLogger().warning(ChatColor.RED + "Unexpected error has occurred while trying to register listener! "
                    + " [" + packetListener.getClass().getName() + "]");

            return;
        }

        this.registeredListeners.compute(channel, (key, value) -> {
            if (value == null) {
                return Lists.newArrayList(packetListener);
            } else {
                value.add(packetListener);

                return value;
            }
        });

        this.plugin.getLogger().info(ChatColor.GREEN + "Registered packet listener " + ChatColor.DARK_GREEN + packetListener.getPacketInfo().getId()
                + ChatColor.GREEN + " with class " + ChatColor.DARK_GREEN  + packetListener.getClass().getName());
    }

    @Override
    public void registerPacket(PacketInfo packetInfo) {
        String packetId = packetInfo.getId();

        if (this.registeredPackets.containsKey(packetId)) {
            throw new IllegalArgumentException("This packet has been already registered!");
        }

        this.registeredPackets.put(packetId, packetInfo);
        this.registeredPacketClasses.put(packetId, packetInfo.getPacketClass());

        this.plugin.getLogger().info(ChatColor.GREEN + "Registered packet data " + ChatColor.DARK_GREEN + packetInfo.getId()
            + ChatColor.GREEN + " with class " + ChatColor.DARK_GREEN + packetInfo.getPacketClass().getName());
    }

    @Override
    public Class<? extends Packet> getPacketClass(String packetId) {
        return this.registeredPacketClasses.get(packetId);
    }

    @Override
    public PacketInfo getPacketInfo(String packetId) {
        return this.registeredPackets.get(packetId);
    }

    protected void packetReceived(String packetChannel, String jsonPacket) {
        this.plugin.getLogger().info(ChatColor.YELLOW + "Received packet on channel: " + ChatColor.GOLD + packetChannel);
        this.plugin.getLogger().info(ChatColor.YELLOW + "Packet data: " + ChatColor.GOLD + jsonPacket);

        List<PacketListener<? extends Packet>> packetListeners = this.registeredListeners.get(packetChannel);
        if (packetListeners == null) {
            this.plugin.getLogger().info(ChatColor.RED + "Received packet but no listeners for that packet has been registered! Channel: " + packetChannel);

            return;
        }

        for (PacketListener<? extends Packet> packetListener : packetListeners) {
            try {
                this.plugin.getLogger().info(ChatColor.YELLOW + "Forwarding packet " + packetChannel + " to " + packetListener.getClass().getName());
                packetListener.process(jsonPacket);
            } catch (NotActiveException e) {
                this.plugin.getLogger().warning(ChatColor.RED + "Cannot process packet in not registered listener!"
                        + " [" + packetListener.getClass().getName() + "]");
                this.plugin.getLogger().warning(ChatColor.RED + "Packet data: " + jsonPacket);
            } catch (MalformedJsonException e) {
                this.plugin.getLogger().warning(ChatColor.DARK_RED + "Received malformed packet!"
                        + " [" + packetListener.getClass().getName() + "]");
                this.plugin.getLogger().warning(ChatColor.RED + "Packet data: " + jsonPacket);
            }
        }
    }

    private void registerDefaultPackets() {
        this.registerPacket(LocalPacketRegistry.SERVER.REGISTRATION_REQUEST);
        this.registerPacket(LocalPacketRegistry.SERVER.REGISTRATION_RESPONSE);
        this.registerPacket(LocalPacketRegistry.ECONOMY.REGISTRATION_REQUEST);
        this.registerPacket(LocalPacketRegistry.ECONOMY.REGISTRATION_RESPONSE);
        this.registerPacket(LocalPacketRegistry.ECONOMY.REGISTERED);
        this.registerPacket(LocalPacketRegistry.ECONOMY.BALANCE_UPDATED);
    }
}
