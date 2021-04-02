package pl.glmc.core.common.packets.server;

import pl.glmc.api.common.packet.PacketInfo;
import pl.glmc.core.common.packets.LocalPacketRegistry;
import pl.glmc.api.common.packet.RequestPacket;

public class ServerRegistrationRequest extends RequestPacket {
    private final static PacketInfo PACKET_INFO = LocalPacketRegistry.SERVER.REGISTRATION_REQUEST;

    private final String serverId;

    public ServerRegistrationRequest(final String serverId) {

        this.serverId = serverId;
    }

    public String getServerId() {
        return serverId;
    }

    @Override
    public String getPacketId() {
        return PACKET_INFO.getId();
    }
}