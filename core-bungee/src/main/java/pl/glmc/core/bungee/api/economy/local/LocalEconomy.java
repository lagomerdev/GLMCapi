package pl.glmc.core.bungee.api.economy.local;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import pl.glmc.api.common.EconomyType;
import pl.glmc.api.common.config.EconomyConfig;
import pl.glmc.api.common.economy.Economy;
import pl.glmc.core.bungee.GlmcCoreBungee;
import pl.glmc.core.bungee.api.GlmcApiProvider;

import java.util.UUID;

public class LocalEconomy {
    private final GlmcCoreBungee plugin;
    private final GlmcApiProvider glmcApiProvider;

    private final Economy playerBankEconomy, playerCashEconomy;

    public LocalEconomy(GlmcCoreBungee plugin, GlmcApiProvider glmcApiProvider) {
        this.plugin = plugin;
        this.glmcApiProvider = glmcApiProvider;

        EconomyConfig playerBankConfig = new EconomyConfig("bank", EconomyType.BANK);
        this.playerBankEconomy = this.glmcApiProvider.getEconomyFactory().registerEconomy(playerBankConfig);

        LocalEconomyCacheListener playerBankCacheListener = new LocalEconomyCacheListener(this.playerBankEconomy);
        this.plugin.getProxy().getPluginManager().registerListener(this.plugin, playerBankCacheListener);

        EconomyConfig playerCashConfig = new EconomyConfig("cash", EconomyType.CASH);
        this.playerCashEconomy = this.glmcApiProvider.getEconomyFactory().registerEconomy(playerCashConfig);

        LocalEconomyCacheListener playerCashCacheListener = new LocalEconomyCacheListener(this.playerCashEconomy);
        this.plugin.getProxy().getPluginManager().registerListener(this.plugin, playerCashCacheListener);

        this.plugin.getProxy().getScheduler().runAsync(this.plugin, () -> {
            for (ProxiedPlayer onlinePlayer : this.plugin.getProxy().getPlayers()) {
                UUID playerUUID = onlinePlayer.getUniqueId();

                this.playerBankEconomy.cacheAccount(playerUUID);
                this.playerCashEconomy.cacheAccount(playerUUID);
            }
        });
    }

    public Economy getPlayerBankEconomy() {
        return playerBankEconomy;
    }

    public Economy getPlayerCashEconomy() {
        return playerCashEconomy;
    }
}
