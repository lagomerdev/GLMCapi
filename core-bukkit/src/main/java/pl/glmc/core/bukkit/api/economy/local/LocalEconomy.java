package pl.glmc.core.bukkit.api.economy.local;

import org.bukkit.entity.Player;
import pl.glmc.api.common.EconomyType;
import pl.glmc.api.common.config.EconomyConfig;
import pl.glmc.api.common.economy.Economy;
import pl.glmc.api.common.economy.EconomyListener;
import pl.glmc.core.bukkit.GlmcCoreBukkit;
import pl.glmc.core.bukkit.api.economy.ApiEconomyProvider;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class LocalEconomy implements EconomyListener {
    private final GlmcCoreBukkit plugin;

    private Economy playerBankEconomy, playerCashEconomy;

    public LocalEconomy(GlmcCoreBukkit plugin) {
        this.plugin = plugin;

        this.plugin.getApiProvider().getEconomyFactory().registerListener(this);

        TestListener testListener = new TestListener(this.plugin, this);
    }

    public Economy getPlayerBankEconomy() {
        return this.playerBankEconomy;
    }

    public Economy getPlayerCashEconomy() {
        return this.playerCashEconomy;
    }

    @Override
    public void loaded(Economy economy) {
        if (economy.getEconomyConfig().getName().equals("bank")) {
            this.playerBankEconomy = economy;

            LocalEconomyCacheListener playerBankCacheListener = new LocalEconomyCacheListener(this.playerBankEconomy);
            this.plugin.getServer().getPluginManager().registerEvents(playerBankCacheListener, this.plugin);

            this.cacheAll(economy);
        } else if (economy.getEconomyConfig().getName().equals("cash")) {
            this.playerCashEconomy = economy;

            LocalEconomyCacheListener playerCashCacheListener = new LocalEconomyCacheListener(this.playerCashEconomy);
            this.plugin.getServer().getPluginManager().registerEvents(playerCashCacheListener, this.plugin);

            this.cacheAll(economy);
        }
    }

    private void cacheAll(Economy economy) {
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> {
            for (Player onlinePlayer : this.plugin.getServer().getOnlinePlayers()) {
                UUID playerUUID = onlinePlayer.getUniqueId();

                economy.cacheAccount(playerUUID);
            }
        });
    }
}