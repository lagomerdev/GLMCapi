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

    private final Economy playerBankEconomy, playerCashEconomy;

    public LocalEconomy(GlmcCoreBungee plugin) {
        this.plugin = plugin;

        EconomyConfig playerBankConfig = new EconomyConfig("bank", EconomyType.BANK);
        this.playerBankEconomy = this.plugin.getApiProvider().getEconomyFactory().loadEconomy(playerBankConfig);

        LocalEconomyCacheListener playerBankCacheListener = new LocalEconomyCacheListener(this.playerBankEconomy);
        this.plugin.getProxy().getPluginManager().registerListener(this.plugin, playerBankCacheListener);

        EconomyConfig playerCashConfig = new EconomyConfig("cash", EconomyType.CASH);
        this.playerCashEconomy = this.plugin.getApiProvider().getEconomyFactory().loadEconomy(playerCashConfig);

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