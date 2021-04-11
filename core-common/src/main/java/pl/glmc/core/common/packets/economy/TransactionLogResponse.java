package pl.glmc.core.common.packets.economy;

import pl.glmc.api.common.economy.TransactionLog;
import pl.glmc.api.common.packet.PacketInfo;
import pl.glmc.api.common.packet.ResponsePacket;
import pl.glmc.core.common.packets.LocalPacketRegistry;

import java.util.UUID;

public class TransactionLogResponse extends ResponsePacket {
    private static final PacketInfo PACKET_INFO = LocalPacketRegistry.ECONOMY.TRANSACTION_LOG_RESPONSE;

    private final TransactionLog transactionLog;

    public TransactionLogResponse(boolean success, UUID originUniqueId, TransactionLog transactionLog) {
        super(success, originUniqueId);

        this.transactionLog = transactionLog;
    }

    public TransactionLog getTransactionLog() {
        return transactionLog;
    }

    @Override
    public String getPacketId() {
        return PACKET_INFO.getId();
    }
}
