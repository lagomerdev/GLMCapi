package pl.glmc.core.common.packets.economy;

import pl.glmc.api.common.packet.PacketInfo;
import pl.glmc.api.common.packet.ResponsePacket;
import pl.glmc.core.common.packets.LocalPacketRegistry;

import java.math.BigDecimal;
import java.util.UUID;

public class BalanceResponse extends ResponsePacket {
    private static final PacketInfo PACKET_INFO = LocalPacketRegistry.ECONOMY.BALANCE_RESPONSE;

    private final BigDecimal balance;

    public BalanceResponse(boolean success, UUID originUniqueId, BigDecimal balance) {
        super(success, originUniqueId);

        this.balance = balance;
    }



    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public String getPacketId() {
        return PACKET_INFO.getId();
    }
}
