package pl.glmc.core.bukkit.api;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import pl.glmc.api.bukkit.GlmcApiBukkit;
import pl.glmc.api.bukkit.GlmcApiBukkitProvider;
import pl.glmc.api.bukkit.packet.PacketService;
import pl.glmc.api.bukkit.user.UserManager;
import pl.glmc.api.common.LuckPermsHook;
import pl.glmc.api.common.economy.Economy;
import pl.glmc.api.common.economy.EconomyFactory;
import pl.glmc.core.bukkit.GlmcCoreBukkit;
import pl.glmc.core.bukkit.api.economy.ApiEconomyFactory;
import pl.glmc.core.bukkit.api.economy.local.LocalEconomy;
import pl.glmc.core.bukkit.api.hook.ApiLuckPermsHook;
import pl.glmc.core.bukkit.api.packet.ApiNetworkService;
import pl.glmc.core.bukkit.api.packet.ApiPacketService;
import pl.glmc.core.bukkit.api.server.ServerManager;

import java.math.BigDecimal;

public class GlmcApiProvider implements GlmcApiBukkit {
    private final GlmcCoreBukkit plugin;

    private ApiPacketService packetService;
    private ApiNetworkService networkService;

    private ApiEconomyFactory economyFactory;
    private LocalEconomy localEconomy;

    private LuckPermsHook luckPermsHook;

    private ServerManager serverManager;


    public GlmcApiProvider(GlmcCoreBukkit plugin) {
        this.plugin = plugin;
    }

    public void load() {
        this.packetService = new ApiPacketService(this.plugin);
        this.networkService = new ApiNetworkService(this.plugin, this.packetService);

        this.luckPermsHook = new ApiLuckPermsHook(this.plugin);

        this.economyFactory = new ApiEconomyFactory(this.plugin);
        this.localEconomy = new LocalEconomy(this.plugin);

        this.serverManager = new ServerManager(this.plugin);

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
        return this.localEconomy.getPlayerCashEconomy();
    }

    @Override
    public PacketService getPacketService() {
        return this.packetService;
    }

    @Override
    public UserManager getUserManager() {
        return null;
    }

    @Override
    public void unload(Plugin plugin) {
        this.packetService.unregister(plugin);
    }

    @Override
    public String getServerId() {
        return this.plugin.getServerId();
    }
}
