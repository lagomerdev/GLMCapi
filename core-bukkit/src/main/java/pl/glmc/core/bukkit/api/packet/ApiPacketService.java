package pl.glmc.core.bukkit.api.packet;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.stream.MalformedJsonException;
import net.md_5.bungee.api.ChatColor;
import pl.glmc.api.common.packet.Packet;
import pl.glmc.api.common.packet.PacketInfo;
import pl.glmc.api.common.packet.PacketListener;
import pl.glmc.api.common.packet.PacketService;
import pl.glmc.core.bukkit.GlmcCoreBukkit;
import pl.glmc.core.common.packets.LocalPacketRegistry;
import pl.glmc.core.common.packets.economy.EconomyRegistrationRequest;
import pl.glmc.core.common.packets.server.ServerRegistrationRequest;

import java.io.NotActiveException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public class ApiPacketService implements PacketService {
    private final GlmcCoreBukkit plugin;

    private final HashMap<String, List<PacketListener<? extends Packet>>> registeredListeners;
    private final HashMap<String, Class<? extends Packet>> registeredPacketClasses;
    private final HashMap<String, PacketInfo> registeredPackets;

    public ApiPacketService(final GlmcCoreBukkit plugin) {
        this.plugin = plugin;

        this.registeredListeners = new HashMap<>();
        this.registeredPackets = new HashMap<>();
        this.registeredPacketClasses = new HashMap<>();

        this.registerDefaultPackets();
    }

    @Override
    public void sendPacket(final Packet packet, final String target) {
        String jsonPacket = this.plugin.getGson().toJson(packet);
        String packetChannel = target + "." + packet.getPacketId();

        this.plugin.getRedisProvider().publish(packetChannel, jsonPacket);
    }

    @Override
    public void registerListener(PacketListener<? extends Packet> packetListener) {
        String channel = packetListener.getPacketInfo().getId();

        try {
            Method registrationMethod = packetListener.getClass().getSuperclass().getDeclaredMethod("register", Gson.class);
            registrationMethod.setAccessible(true);
            registrationMethod.invoke(packetListener, this.plugin.getGson());
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
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
        this.plugin.getLogger().info(ChatColor.YELLOW + "Received packet on channel " + ChatColor.GOLD + packetChannel);

        List<PacketListener<? extends Packet>> packetListeners = this.registeredListeners.get(packetChannel);
        if (packetListeners == null) {
            this.plugin.getLogger().info(ChatColor.RED + "Received packet but no listeners for that packet has been registered! Channel: " + packetChannel);

            return;
        }

        for (PacketListener<? extends Packet> packetListener : packetListeners) {
            try {
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
    }
}
