package pl.glmc.core.common.packets.economy;

import pl.glmc.api.common.packet.InfoPacket;
import pl.glmc.api.common.packet.PacketInfo;
import pl.glmc.core.common.packets.LocalPacketRegistry;

import java.math.BigDecimal;
import java.util.UUID;

public class BalanceUpdated extends InfoPacket {
    private final static PacketInfo PACKET_INFO = LocalPacketRegistry.ECONOMY.BALANCE_UPDATED;

    private final UUID accountUniqueId;
    private final BigDecimal balance;

    public BalanceUpdated(UUID accountUniqueId, BigDecimal balance) {
        this.accountUniqueId = accountUniqueId;
        this.balance = balance;
    }

    public UUID getAccountUniqueId() {
        return accountUniqueId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public String getPacketId() {
        return PACKET_INFO.getId();
    }
}
