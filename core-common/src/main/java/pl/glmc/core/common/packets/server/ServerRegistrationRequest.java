package pl.glmc.core.common.packets.server;

import pl.glmc.api.common.packet.PacketInfo;
import pl.glmc.api.common.server.Server;
import pl.glmc.core.common.packets.LocalPacketRegistry;
import pl.glmc.api.common.packet.RequestPacket;

public class ServerRegistrationRequest extends RequestPacket {
    private final static PacketInfo PACKET_INFO = LocalPacketRegistry.SERVER.REGISTRATION_REQUEST;

    private final Server server;

    public ServerRegistrationRequest(final Server server) {
        this.server = server;
    }

    public Server getServer() {
        return server;
    }

    @Override
    public String getPacketId() {
        return PACKET_INFO.getId();
    }
}