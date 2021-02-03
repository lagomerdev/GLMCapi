package pl.glmc.core.bukkit.api.economy;

import org.bukkit.entity.Player;
import pl.glmc.api.common.EconomyType;
import pl.glmc.api.common.config.EconomyConfig;
import pl.glmc.api.common.economy.Economy;
import pl.glmc.core.bukkit.GlmcCoreBukkit;
import pl.glmc.core.bukkit.api.GlmcApiProvider;

import java.util.UUID;

public class LocalEconomy {
    private final GlmcCoreBukkit plugin;
    //private final GlmcApiProvider glmcApiProvider;

    private final Economy playerBankEconomy, playerCashEconomy;

    public LocalEconomy(GlmcCoreBukkit plugin, GlmcApiProvider glmcApiProvider) {
        this.plugin = plugin;
        //this.glmcApiProvider = glmcApiProvider;

        EconomyConfig playerBankConfig = new EconomyConfig("bank", EconomyType.BANK);
        this.playerBankEconomy = glmcApiProvider.getEconomyFactory().registerEconomy(playerBankConfig);

        LocalCacheListener playerBankCacheListener = new LocalCacheListener(this.playerBankEconomy, this);
        this.plugin.getServer().getPluginManager().registerEvents(playerBankCacheListener, this.plugin);

        EconomyConfig playerCashConfig = new EconomyConfig("cash", EconomyType.CASH);
        this.playerCashEconomy = glmcApiProvider.getEconomyFactory().registerEconomy(playerCashConfig);

        LocalCacheListener playerCashCacheListener = new LocalCacheListener(this.playerCashEconomy, this);
        this.plugin.getServer().getPluginManager().registerEvents(playerCashCacheListener, this.plugin);

        this.plugin.getServer().getScheduler().runTask(this.plugin, () -> {
            for (Player onlinePlayer : this.plugin.getServer().getOnlinePlayers()) {
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
