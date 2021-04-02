package pl.glmc.core.bukkit.api;

import org.bukkit.ChatColor;
import pl.glmc.api.bukkit.GlmcApiBukkit;
import pl.glmc.api.bukkit.GlmcApiBukkitProvider;
import pl.glmc.api.common.LuckPermsHook;
import pl.glmc.api.common.economy.Economy;
import pl.glmc.api.common.economy.EconomyFactory;
import pl.glmc.api.common.packet.PacketService;
import pl.glmc.core.bukkit.GlmcCoreBukkit;
import pl.glmc.core.bukkit.api.hook.ApiLuckPermsHook;
import pl.glmc.core.bukkit.api.packet.ApiNetworkService;
import pl.glmc.core.bukkit.api.packet.ApiPacketService;

public class GlmcApiProvider implements GlmcApiBukkit {
    private final GlmcCoreBukkit plugin;

    private final LuckPermsHook luckPermsHook;
    private final ApiPacketService packetService;
    private final ApiNetworkService networkService;

    public GlmcApiProvider(GlmcCoreBukkit plugin) {
        this.plugin = plugin;

        this.luckPermsHook = new ApiLuckPermsHook(this.plugin);

        this.packetService = new ApiPacketService(this.plugin);
        this.networkService = new ApiNetworkService(this.plugin, this.packetService);
        GlmcApiBukkitProvider.register(this);

        this.plugin.getLogger().info(ChatColor.DARK_GREEN + "Loaded API Provider");
    }

    @Override
    public LuckPermsHook getLuckPermsHook() {
        return this.luckPermsHook;
    }

    @Override
    public EconomyFactory getEconomyFactory() {
        return null;
    }

    @Override
    public Economy getPlayerBankEconomy() {
        return null;
    }

    @Override
    public Economy getPlayerCashEconomy() {
        return null;
    }

    @Override
    public PacketService getPacketService() {
        return this.packetService;
    }
}
