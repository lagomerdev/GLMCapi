package pl.glmc.core.bungee.api;

import net.md_5.bungee.api.ChatColor;
import pl.glmc.api.bungee.GlmcApiBungee;
import pl.glmc.api.bungee.GlmcApiBungeeProvider;
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

public class GlmcApiProvider implements GlmcApiBungee {
    private final GlmcCoreBungee plugin;

    private final ApiLuckPermsHook luckPermsHook;

    private final ApiEconomyFactory economyFactory;
    private final LocalEconomy localEconomy;

    private final ApiPacketService packetService;
    private final ApiNetworkService networkService;

    public GlmcApiProvider(GlmcCoreBungee plugin) {
        this.plugin = plugin;

        this.luckPermsHook = new ApiLuckPermsHook(this.plugin);

        this.economyFactory = new ApiEconomyFactory(this.plugin);
        this.localEconomy = new LocalEconomy(this.plugin, this);

        this.packetService = new ApiPacketService(this.plugin);
        this.networkService = new ApiNetworkService(this.plugin, this.packetService);

        GlmcApiBungeeProvider.register(this);

        this.plugin.getLogger().info(ChatColor.DARK_GREEN + "Loaded Bungee API Provider");
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

    @Override
    public PacketService getPacketService() {
        return this.packetService;
    }
}
