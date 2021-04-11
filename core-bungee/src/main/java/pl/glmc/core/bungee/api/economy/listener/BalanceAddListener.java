package pl.glmc.core.bungee.api.economy.listener;

import pl.glmc.api.common.packet.listener.PacketListener;
import pl.glmc.core.bungee.GlmcCoreBungee;
import pl.glmc.core.bungee.api.economy.ApiEconomyProvider;
import pl.glmc.core.common.packets.LocalPacketRegistry;
import pl.glmc.core.common.packets.economy.BalanceAddRequest;
import pl.glmc.core.common.packets.economy.BalanceAddResponse;

public class BalanceAddListener extends PacketListener<BalanceAddRequest> {
    private final ApiEconomyProvider economyProvider;
    private final GlmcCoreBungee plugin;

    public BalanceAddListener(final GlmcCoreBungee plugin, final ApiEconomyProvider economyProvider) {
        super(LocalPacketRegistry.ECONOMY.BALANCE_ADD_REQUEST, BalanceAddRequest.class);

        this.economyProvider = economyProvider;
        this.plugin = plugin;
    }

    @Override
    public void received(BalanceAddRequest packet) {
        this.economyProvider.add(packet.getAccountUniqueId(), packet.getAmount())
                .thenAccept(success -> {
                    BalanceAddResponse response = new BalanceAddResponse(success, packet.getUniqueId());

                    this.plugin.getApiProvider().getPacketService().sendPacket(response, packet.getSender(), this.economyProvider.getEconomyConfig().getName());
                });
    }
}
