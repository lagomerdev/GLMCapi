package pl.glmc.core.bungee.api.economy.listener;

import pl.glmc.api.common.packet.PacketInfo;
import pl.glmc.api.common.packet.listener.PacketListener;
import pl.glmc.core.bungee.GlmcCoreBungee;
import pl.glmc.core.bungee.api.economy.ApiEconomyFactory;
import pl.glmc.core.bungee.api.economy.ApiEconomyProvider;
import pl.glmc.core.common.packets.LocalPacketRegistry;
import pl.glmc.core.common.packets.economy.BalanceRequest;
import pl.glmc.core.common.packets.economy.BalanceResponse;

public class BalanceInfoListener extends PacketListener<BalanceRequest> {
    private final ApiEconomyProvider economyProvider;
    private final GlmcCoreBungee plugin;

    public BalanceInfoListener(final GlmcCoreBungee plugin, final ApiEconomyProvider economyProvider) {
        super(LocalPacketRegistry.ECONOMY.BALANCE_REQUEST, BalanceRequest.class);

        this.plugin = plugin;
        this.economyProvider = economyProvider;
    }

    @Override
    public void received(BalanceRequest packet) {
        this.economyProvider.getBalance(packet.getAccountUniqueId())
                .thenAccept(balance -> {
                    boolean success = balance != null;

                    BalanceResponse response = new BalanceResponse(success, packet.getUniqueId(), balance);

                    this.plugin.getApiProvider().getPacketService().sendPacket(response, packet.getSender(), economyProvider.getEconomyConfig().getName());
                });
    }
}
