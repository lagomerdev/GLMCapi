package pl.glmc.core.bungee.api.economy.listener;

import pl.glmc.api.common.packet.listener.PacketListener;
import pl.glmc.core.bungee.GlmcCoreBungee;
import pl.glmc.core.bungee.api.economy.ApiEconomyProvider;
import pl.glmc.core.common.packets.LocalPacketRegistry;
import pl.glmc.core.common.packets.economy.BalanceSetRequest;
import pl.glmc.core.common.packets.economy.BalanceSetResponse;

public class BalanceSetListener extends PacketListener<BalanceSetRequest> {
    private final ApiEconomyProvider economyProvider;
    private final GlmcCoreBungee plugin;

    public BalanceSetListener(final GlmcCoreBungee plugin, final ApiEconomyProvider economyProvider) {
        super(LocalPacketRegistry.ECONOMY.BALANCE_SET_REQUEST, BalanceSetRequest.class);

        this.economyProvider = economyProvider;
        this.plugin = plugin;
    }

    @Override
    public void received(BalanceSetRequest packet) {
        this.economyProvider.set(packet.getAccountUniqueId(), packet.getAmount())
                .thenAccept(success -> {
                    BalanceSetResponse response = new BalanceSetResponse(success, packet.getUniqueId());

                    this.plugin.getApiProvider().getPacketService().sendPacket(response, packet.getSender(), this.economyProvider.getEconomyConfig().getName());
                });
    }
}
