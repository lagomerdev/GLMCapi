package pl.glmc.core.common.packets.economy;

import pl.glmc.api.common.packet.PacketInfo;
import pl.glmc.api.common.packet.ResponsePacket;
import pl.glmc.core.common.packets.LocalPacketRegistry;

import java.util.UUID;

public class BalanceAddResponse extends ResponsePacket {
    private static final PacketInfo PACKET_INFO = LocalPacketRegistry.ECONOMY.BALANCE_ADD_RESPONSE;

    public BalanceAddResponse(boolean success, UUID originUniqueId) {
        super(success, originUniqueId);
    }

    @Override
    public String getPacketId() {
        return PACKET_INFO.getId();
    }
}
