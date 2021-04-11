package pl.glmc.core.common.packets.economy;

import pl.glmc.api.common.packet.PacketInfo;
import pl.glmc.core.common.packets.LocalPacketRegistry;
import pl.glmc.api.common.packet.ResponsePacket;

import java.util.UUID;

public class EconomyRegistrationResponse extends ResponsePacket {
    private final static PacketInfo PACKET_INFO = LocalPacketRegistry.SERVER.REGISTRATION_RESPONSE;

    public EconomyRegistrationResponse(final boolean success, final UUID originUniqueId) {
        super(success, originUniqueId);
    }

    @Override
    public String getPacketId() {
        return PACKET_INFO.getId();
    }
}