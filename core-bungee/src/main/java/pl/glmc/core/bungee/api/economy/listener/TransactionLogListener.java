package pl.glmc.core.bungee.api.economy.listener;

import pl.glmc.api.common.packet.listener.PacketListener;
import pl.glmc.core.bungee.GlmcCoreBungee;
import pl.glmc.core.bungee.api.economy.ApiEconomyProvider;
import pl.glmc.core.common.packets.LocalPacketRegistry;
import pl.glmc.core.common.packets.economy.TransactionLogRequest;
import pl.glmc.core.common.packets.economy.TransactionLogResponse;

public class TransactionLogListener extends PacketListener<TransactionLogRequest> {
    private final GlmcCoreBungee plugin;
    private final ApiEconomyProvider economyProvider;

    public TransactionLogListener(final GlmcCoreBungee plugin, final ApiEconomyProvider economyProvider) {
        super(LocalPacketRegistry.ECONOMY.TRANSACTION_LOG_REQUEST, TransactionLogRequest.class);

        this.plugin = plugin;
        this.economyProvider = economyProvider;
    }

    @Override
    public void received(TransactionLogRequest packet) {
        this.economyProvider.getTransactionLog(packet.getAccountUniqueId(), packet.getLimit(), packet.getSortingMode(), packet.getOrderBy())
                .thenAccept(transactionLog -> {
                    boolean success = transactionLog.getAmount() != 0;

                    TransactionLogResponse response = new TransactionLogResponse(success, packet.getUniqueId(), transactionLog);

                    this.plugin.getApiProvider().getPacketService().sendPacket(response, packet.getSender(), economyProvider.getEconomyConfig().getName());
                });
    }
}
