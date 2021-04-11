package pl.glmc.core.common.packets.economy;

import pl.glmc.api.common.packet.PacketInfo;
import pl.glmc.api.common.packet.ResponsePacket;
import pl.glmc.core.common.packets.LocalPacketRegistry;

import java.util.UUID;

public class AccountExistsResponse extends ResponsePacket {
    private static final PacketInfo PACKET_INFO = LocalPacketRegistry.ECONOMY.ACCOUNT_EXISTS_RESPONSE;

    public AccountExistsResponse(boolean success, UUID originUniqueId) {
        super(success, originUniqueId);
    }

    @Override
    public String getPacketId() {
        return PACKET_INFO.getId();
    }
}
