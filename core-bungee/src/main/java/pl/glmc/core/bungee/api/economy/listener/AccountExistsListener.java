package pl.glmc.core.bungee.api.economy.listener;

import pl.glmc.api.common.packet.listener.PacketListener;
import pl.glmc.core.bungee.GlmcCoreBungee;
import pl.glmc.core.bungee.api.economy.ApiEconomyProvider;
import pl.glmc.core.common.packets.LocalPacketRegistry;
import pl.glmc.core.common.packets.economy.AccountExistsRequest;
import pl.glmc.core.common.packets.economy.AccountExistsResponse;

public class AccountExistsListener extends PacketListener<AccountExistsRequest> {
    private final GlmcCoreBungee plugin;
    private final ApiEconomyProvider economyProvider;

    public AccountExistsListener(final GlmcCoreBungee plugin, final ApiEconomyProvider economyProvider) {
        super(LocalPacketRegistry.ECONOMY.ACCOUNT_EXISTS_REQUEST, AccountExistsRequest.class);

        this.plugin = plugin;
        this.economyProvider = economyProvider;
    }

    @Override
    public void received(AccountExistsRequest packet) {
        this.economyProvider.accountExists(packet.getAccountUniqueId())
                .thenAccept(success -> {
                    AccountExistsResponse response = new AccountExistsResponse(success, packet.getUniqueId());

                    this.plugin.getApiProvider().getPacketService().sendPacket(response, packet.getSender(), this.economyProvider.getEconomyConfig().getName());
                });
    }
}
