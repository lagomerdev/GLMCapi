package pl.glmc.api.bukkit.packet;

import org.bukkit.plugin.Plugin;
import pl.glmc.api.common.packet.Packet;
import pl.glmc.api.common.packet.listener.PacketListener;

public interface PacketService {

    /**
     * Sends packet to given target server
     *
     * @param packet
     * @param target
     */
    void sendPacket(Packet packet, String target);

    /**
     *
     * @param packet
     * @param target
     * @param additionalId
     */
    void sendPacket(Packet packet, String target, String additionalId);

    /**
     *
     * @param packetListener
     * @param plugin
     */
    void registerListener(PacketListener<? extends Packet> packetListener, Plugin plugin);

    /**
     *
     * @param packetListener
     * @param additionalId
     * @param plugin
     */
    void registerListener(PacketListener<? extends Packet> packetListener, String additionalId, Plugin plugin);

    /**
     *
     * @param plugin
     */
    void unregister(Plugin plugin);
}
