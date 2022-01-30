package pl.glmc.core.common.packets.server;

import pl.glmc.api.common.packet.PacketInfo;
import pl.glmc.core.common.packets.LocalPacketRegistry;
import pl.glmc.api.common.packet.ResponsePacket;

import java.util.UUID;

public class ServerRegistrationResponse extends ResponsePacket {
    private final static PacketInfo PACKET_INFO = LocalPacketRegistry.SERVER.REGISTRATION_RESPONSE;

    public ServerRegistrationResponse(final boolean success, final UUID originUniqueId) {
        super(success, originUniqueId);
    }

    @Override
    public String getPacketId() {
        return PACKET_INFO.getId();
    }
}