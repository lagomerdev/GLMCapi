package pl.glmc.api.common.packet;

public class PacketInfo {
    private final String id;
    private final Class<? extends Packet> packetClass;

    private PacketInfo(final String id, final Class<? extends Packet> packetClass) {
        this.id = id;
        this.packetClass = packetClass;
    }

    public String getId() {
        return id;
    }

    public Class<? extends Packet> getPacketClass() {
        return packetClass;
    }

    public static PacketInfo make(final String id, final Class<? extends Packet> packetClass) {
        return new PacketInfo(id, packetClass);
    }
}
