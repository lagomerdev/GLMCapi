package pl.glmc.api.common.packet;

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
     */
    void registerListener(PacketListener<? extends Packet> packetListener);

    /**
     *
     * @param packetListener
     * @param additionalId
     */
    void registerListener(PacketListener<? extends Packet> packetListener, String additionalId);

    /**
     *
     * @param packetInfo
     */
    void registerPacket(PacketInfo packetInfo);

    /**
     *
     * @param packetId
     * @return
     */
    Class<? extends Packet> getPacketClass(String packetId);

    /**
     *
     * @param packetId
     * @return
     */
    PacketInfo getPacketInfo(String packetId);
}
