package pl.glmc.core.bukkit.api;

import org.bukkit.ChatColor;
import pl.glmc.api.bukkit.GlmcApiBukkit;
import pl.glmc.api.bukkit.GlmcApiBukkitProvider;
import pl.glmc.api.common.LuckPermsHook;
import pl.glmc.api.common.economy.Economy;
import pl.glmc.api.common.economy.EconomyFactory;
import pl.glmc.core.bukkit.GlmcCoreBukkit;
import pl.glmc.core.bukkit.api.economy.LocalEconomy;
import pl.glmc.core.bukkit.api.economy.ApiEconomyFactory;
import pl.glmc.core.bukkit.api.hook.ApiLuckPermsHook;

public class GlmcApiProvider implements GlmcApiBukkit {
    private final GlmcCoreBukkit plugin;

    private final LuckPermsHook luckPermsHook;
    private final EconomyFactory economyFactory;
    private final LocalEconomy localEconomy;

    public GlmcApiProvider(GlmcCoreBukkit plugin) {
        this.plugin = plugin;

        this.luckPermsHook = new ApiLuckPermsHook(this.plugin);
        this.economyFactory = new ApiEconomyFactory(this.plugin);
        this.localEconomy = new LocalEconomy(this.plugin, this);

        GlmcApiBukkitProvider.register(this);

        this.plugin.getLogger().info(ChatColor.DARK_GREEN + "Loaded API Provider");
    }

    @Override
    public LuckPermsHook getLuckPermsHook() {
        return this.luckPermsHook;
    }

    @Override
    public EconomyFactory getEconomyFactory() {
        return this.economyFactory;
    }

    @Override
    public Economy getPlayerBankEconomy() {
        return this.localEconomy.getPlayerBankEconomy();
    }

    @Override
    public Economy getPlayerCashEconomy() {
        return this.localEconomy.getPlayerBankEconomy();
    }
}
