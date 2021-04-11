package pl.glmc.core.bungee.api.economy.listener;

import pl.glmc.api.common.packet.listener.PacketListener;
import pl.glmc.core.bungee.GlmcCoreBungee;
import pl.glmc.core.bungee.api.economy.ApiEconomyProvider;
import pl.glmc.core.common.packets.LocalPacketRegistry;
import pl.glmc.core.common.packets.economy.AccountCreateRequest;
import pl.glmc.core.common.packets.economy.AccountCreateResponse;

public class AccountCreateListener extends PacketListener<AccountCreateRequest> {
    private final GlmcCoreBungee plugin;
    private final ApiEconomyProvider economyProvider;

    public AccountCreateListener(final GlmcCoreBungee plugin, final ApiEconomyProvider economyProvider) {
        super(LocalPacketRegistry.ECONOMY.ACCOUNT_CREATE_REQUEST, AccountCreateRequest.class);

        this.plugin = plugin;
        this.economyProvider = economyProvider;
    }

    @Override
    public void received(AccountCreateRequest packet) {
        this.economyProvider.createAccount(packet.getAccountUniqueId())
                .thenAccept(success -> {
                    AccountCreateResponse response = new AccountCreateResponse(success, packet.getUniqueId());

                    this.plugin.getApiProvider().getPacketService().sendPacket( response, packet.getSender(), this.economyProvider.getEconomyConfig().getName());
                });

    }
}
