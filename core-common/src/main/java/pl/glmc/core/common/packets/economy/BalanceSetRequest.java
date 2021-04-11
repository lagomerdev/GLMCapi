package pl.glmc.core.common.packets.economy;

import pl.glmc.api.common.packet.PacketInfo;
import pl.glmc.api.common.packet.RequestPacket;
import pl.glmc.core.common.packets.LocalPacketRegistry;

import java.math.BigDecimal;
import java.util.UUID;

public class BalanceSetRequest extends RequestPacket {
    private static final PacketInfo PACKET_INFO = LocalPacketRegistry.ECONOMY.BALANCE_SET_REQUEST;

    private final UUID accountUniqueId;
    private final BigDecimal amount;

    public BalanceSetRequest(UUID accountUniqueId, BigDecimal amount) {
        this.accountUniqueId = accountUniqueId;
        this.amount = amount;
    }

    public UUID getAccountUniqueId() {
        return accountUniqueId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String getPacketId() {
        return PACKET_INFO.getId();
    }
}
