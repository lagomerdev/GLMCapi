package pl.glmc.core.bukkit.api.economy;

import pl.glmc.api.common.packet.listener.PacketListener;
import pl.glmc.core.bukkit.GlmcCoreBukkit;
import pl.glmc.core.common.packets.LocalPacketRegistry;
import pl.glmc.core.common.packets.economy.BalanceUpdated;

public class ApiEconomyListener extends PacketListener<BalanceUpdated> {
    private final GlmcCoreBukkit plugin;
    private final ApiEconomyProvider apiEconomyProvider;

    public ApiEconomyListener(final GlmcCoreBukkit plugin, final ApiEconomyProvider apiEconomyProvider) {
        super(LocalPacketRegistry.ECONOMY.BALANCE_UPDATED, BalanceUpdated.class);

        this.apiEconomyProvider = apiEconomyProvider;
        this.plugin = plugin;

        this.plugin.getApiProvider().getPacketService().registerListener(this, apiEconomyProvider.getEconomyConfig().getName());
    }

    @Override
    public void received(BalanceUpdated packet) { //todo add registering uuid cache
        if (this.apiEconomyProvider.isCached(packet.getAccountUniqueId())) {
            this.apiEconomyProvider.updateBalance(packet.getAccountUniqueId(), packet.getBalance());
        }
    }
}
