package pl.glmc.api.common.packet;

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
     * @param packetListener
     */
    void registerListener(PacketListener<? extends Packet> packetListener);

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
