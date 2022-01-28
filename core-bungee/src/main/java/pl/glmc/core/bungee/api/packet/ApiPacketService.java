package pl.glmc.core.bungee.api.packet;

import com.google.common.collect.*;
import com.google.gson.Gson;
import com.google.gson.stream.MalformedJsonException;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import pl.glmc.api.bungee.packet.PacketService;
import pl.glmc.api.common.packet.Packet;
import pl.glmc.api.common.packet.listener.PacketListener;
import pl.glmc.core.bungee.GlmcCoreBungee;
;
import java.io.NotActiveException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ApiPacketService implements PacketService {
    private final GlmcCoreBungee plugin;

    private final ListMultimap<String, PacketListener<? extends Packet>> registeredListenersChannels, registeredListenersPlugins;
    private final Method registrationMethod;

    public ApiPacketService(final GlmcCoreBungee plugin) {
        this.plugin = plugin;

        this.registeredListenersChannels = ArrayListMultimap.create();
        this.registeredListenersPlugins = ArrayListMultimap.create();

        try {
            this.registrationMethod = PacketListener.class.getDeclaredMethod("register", Gson.class);
            this.registrationMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            this.plugin.getLogger().warning(ChatColor.RED + "Failed to load packet service!");

            throw new RuntimeException("Failed to load registration method!");
        }
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

        this.plugin.getRedisProvider().publish(packetChannel, jsonPacket);
    }

    @Override
    public void registerListener(PacketListener<? extends Packet> packetListener, Plugin plugin) {
        this.defaultRegisterListener(packetListener, null, plugin);
    }

    @Override
    public void registerListener(PacketListener<? extends Packet> packetListener, String additionalId, Plugin plugin) {
        this.defaultRegisterListener(packetListener, additionalId, plugin);
    }

    @Override
    public void unregister(Plugin plugin) {
        List<PacketListener<? extends Packet>> removedListeners = this.registeredListenersPlugins.removeAll(plugin.getDescription().getName());
        this.registeredListenersChannels.entries().removeIf(entry -> {
            PacketListener<? extends Packet> packetListener = entry.getValue();
            String packetChannel = entry.getKey();

            if (removedListeners.contains(packetListener)) {
                this.plugin.getLogger().info(ChatColor.YELLOW + "Unregistered packet listener " + ChatColor.GOLD + packetChannel
                        + ChatColor.YELLOW + " with class " + ChatColor.GOLD  + packetListener.getClass().getName()
                        + ChatColor.YELLOW + " from plugin " + ChatColor.GOLD  + plugin.getDescription().getName());

                return true;
            } else return false;
        });
    }

    private void defaultRegisterListener(PacketListener<? extends Packet> packetListener, String additionalId, Plugin plugin) {
        String channel = additionalId == null ? packetListener.getPacketInfo().getId() : packetListener.getPacketInfo().getId() + "." + additionalId;

        try {
            this.registrationMethod.invoke(packetListener, this.plugin.getGson());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();

            this.plugin.getLogger().warning(ChatColor.RED + "Unexpected error has occurred while trying to register listener! "
                    + " [" + packetListener.getClass().getName() + "]");

            return;
        }

        if (!this.registeredListenersChannels.containsValue(packetListener)) {
            this.registeredListenersChannels.put(channel, packetListener);
            this.registeredListenersPlugins.put(plugin.getDescription().getName(), packetListener);
        } else {
            throw new RuntimeException("This packet listener has been already registered!");
        }

        this.plugin.getLogger().info(ChatColor.GREEN + "Registered packet listener " + ChatColor.DARK_GREEN + packetListener.getPacketInfo().getId()
                + ChatColor.GREEN + " with class " + ChatColor.DARK_GREEN  + packetListener.getClass().getName()
                + ChatColor.GREEN + " with plugin " + ChatColor.DARK_GREEN + plugin.getDescription().getName());
    }

    protected void packetReceived(String packetChannel, String jsonPacket) {
        this.plugin.getLogger().info(ChatColor.YELLOW + "Received packet on channel: " + ChatColor.GOLD + packetChannel);
        this.plugin.getLogger().info(ChatColor.YELLOW + "Packet data: " + ChatColor.GOLD + jsonPacket);

        List<PacketListener<? extends Packet>> packetListeners = this.registeredListenersChannels.get(packetChannel);
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
}
