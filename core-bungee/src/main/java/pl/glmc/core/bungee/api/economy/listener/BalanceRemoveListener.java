package pl.glmc.core.bungee.api.economy.listener;

import pl.glmc.api.common.packet.listener.PacketListener;
import pl.glmc.core.bungee.GlmcCoreBungee;
import pl.glmc.core.bungee.api.economy.ApiEconomyProvider;
import pl.glmc.core.common.packets.LocalPacketRegistry;
import pl.glmc.core.common.packets.economy.BalanceRemoveRequest;
import pl.glmc.core.common.packets.economy.BalanceRemoveResponse;

public class BalanceRemoveListener extends PacketListener<BalanceRemoveRequest> {
    private final GlmcCoreBungee plugin;
    private final ApiEconomyProvider economyProvider;

    public BalanceRemoveListener(GlmcCoreBungee plugin, ApiEconomyProvider economyProvider) {
        super(LocalPacketRegistry.ECONOMY.BALANCE_REMOVE_REQUEST, BalanceRemoveRequest.class);

        this.plugin = plugin;
        this.economyProvider = economyProvider;
    }

    @Override
    public void received(BalanceRemoveRequest packet) {
        this.economyProvider.remove(packet.getAccountUniqueId(), packet.getAmount())
                .thenAccept(success -> {
                    BalanceRemoveResponse response = new BalanceRemoveResponse(success, packet.getUniqueId());

                    this.plugin.getApiProvider().getPacketService().sendPacket(response, packet.getSender(), this.economyProvider.getEconomyConfig().getName());
                });
    }
}
