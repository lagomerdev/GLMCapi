package pl.glmc.core.bukkit.api.economy.listener;

import pl.glmc.api.common.economy.TransactionLog;
import pl.glmc.api.common.packet.listener.ResponseHandlerListener;
import pl.glmc.core.common.packets.LocalPacketRegistry;
import pl.glmc.core.common.packets.economy.TransactionLogResponse;

public class TransactionLogHandler extends ResponseHandlerListener<TransactionLogResponse, TransactionLog> {
    public TransactionLogHandler() {
        super(LocalPacketRegistry.ECONOMY.TRANSACTION_LOG_RESPONSE, TransactionLogResponse.class);
    }

    @Override
    public void received(TransactionLogResponse packet) {
        this.complete(packet.getOriginUniqueId(), packet.getTransactionLog());
    }
}
