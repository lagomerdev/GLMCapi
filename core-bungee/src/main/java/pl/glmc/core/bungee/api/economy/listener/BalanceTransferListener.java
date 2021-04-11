package pl.glmc.core.bungee.api.economy.listener;

import pl.glmc.api.common.economy.Economy;
import pl.glmc.api.common.packet.listener.PacketListener;
import pl.glmc.core.bungee.GlmcCoreBungee;
import pl.glmc.core.bungee.api.economy.ApiEconomyProvider;
import pl.glmc.core.common.packets.LocalPacketRegistry;
import pl.glmc.core.common.packets.economy.BalanceTransferRequest;
import pl.glmc.core.common.packets.economy.BalanceTransferResponse;

public class BalanceTransferListener extends PacketListener<BalanceTransferRequest> {
    private final GlmcCoreBungee plugin;
    private final ApiEconomyProvider economyProvider;

    public BalanceTransferListener(GlmcCoreBungee plugin, ApiEconomyProvider economyProvider) {
        super(LocalPacketRegistry.ECONOMY.BALANCE_TRANSFER_REQUEST, BalanceTransferRequest.class);

        this.plugin = plugin;
        this.economyProvider = economyProvider;
    }

    @Override
    public void received(BalanceTransferRequest packet) {
        try {
            Economy targetEconomy = this.plugin.getApiProvider().getEconomyFactory().getEconomy(packet.getTargetEconomyName());

            this.economyProvider.transfer(packet.getFromAccountUniqueId(), packet.getToAccountUniqueId(), packet.getAmount(), targetEconomy)
                    .thenAccept(success -> {
                        BalanceTransferResponse response = new BalanceTransferResponse(success, packet.getUniqueId());

                        this.plugin.getApiProvider().getPacketService().sendPacket(response, packet.getSender(), economyProvider.getEconomyConfig().getName());
                    });
        } catch (NullPointerException e) {
            BalanceTransferResponse response = new BalanceTransferResponse(false, packet.getUniqueId());

            this.plugin.getApiProvider().getPacketService().sendPacket(response, packet.getSender(), economyProvider.getEconomyConfig().getName());
        }
    }
}
