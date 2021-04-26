package pl.glmc.core.bungee.api;

import net.luckperms.api.cacheddata.CachedData;
import net.md_5.bungee.api.ChatColor;
import pl.glmc.api.bungee.GlmcApiBungee;
import pl.glmc.api.bungee.GlmcApiBungeeProvider;
import pl.glmc.api.bungee.server.ServerManager;
import pl.glmc.api.bungee.user.UserManager;
import pl.glmc.api.common.LuckPermsHook;
import pl.glmc.api.common.economy.Economy;
import pl.glmc.api.common.economy.EconomyFactory;
import pl.glmc.api.common.packet.PacketService;
import pl.glmc.core.bungee.GlmcCoreBungee;
import pl.glmc.core.bungee.api.economy.ApiEconomyFactory;
import pl.glmc.core.bungee.api.economy.local.LocalEconomy;
import pl.glmc.core.bungee.api.hook.ApiLuckPermsHook;
import pl.glmc.core.bungee.api.packet.ApiNetworkService;
import pl.glmc.core.bungee.api.packet.ApiPacketService;
import pl.glmc.core.bungee.api.server.ApiServerManager;
import pl.glmc.core.bungee.api.user.ApiUserManager;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GlmcApiProvider implements GlmcApiBungee {
    private final GlmcCoreBungee plugin;

    private ApiLuckPermsHook luckPermsHook;

    private ApiEconomyFactory economyFactory;
    private LocalEconomy localEconomy;

    private ApiPacketService packetService;
    private ApiNetworkService networkService;

    private ApiServerManager serverManager;

    private ApiUserManager userManager;

    public GlmcApiProvider(GlmcCoreBungee plugin) {
        this.plugin = plugin;
    }

    public void load() {
        this.packetService = new ApiPacketService(this.plugin);
        this.networkService = new ApiNetworkService(this.plugin, this.packetService);

        this.luckPermsHook = new ApiLuckPermsHook(this.plugin);

        this.economyFactory = new ApiEconomyFactory(this.plugin);
        this.localEconomy = new LocalEconomy(this.plugin);

        this.serverManager = new ApiServerManager(this.plugin);

        this.userManager = new ApiUserManager(this.plugin);

        GlmcApiBungeeProvider.register(this);

        this.plugin.getLogger().info(ChatColor.DARK_GREEN + "Loaded Bungee API Provider");

        this.plugin.getProxy().getScheduler().schedule(this.plugin, () -> {
            this.localEconomy.getPlayerBankEconomy().add(UUID.fromString("5d689771-1ffd-4513-82b8-b58d3f8540da"), new BigDecimal("1000.69"));
        }, 10, TimeUnit.SECONDS);
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
        return this.localEconomy.getPlayerCashEconomy();
    }

    @Override
    public PacketService getPacketService() {
        return this.packetService;
    }

    @Override
    public ServerManager getServerManager() {
        return this.serverManager;
    }

    @Override
    public UserManager getUserManager() {
        return this.userManager;
    }
}
