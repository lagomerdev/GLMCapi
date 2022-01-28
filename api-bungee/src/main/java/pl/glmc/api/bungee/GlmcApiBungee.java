package pl.glmc.api.bungee;

import net.md_5.bungee.api.plugin.Plugin;
import pl.glmc.api.bungee.packet.PacketService;
import pl.glmc.api.bungee.server.ServerManager;
import pl.glmc.api.bungee.user.UserManager;
import pl.glmc.api.common.LuckPermsHook;
import pl.glmc.api.common.economy.Economy;
import pl.glmc.api.common.economy.EconomyFactory;

public interface GlmcApiBungee {

    /**
     * Gets LuckPermsHook instance which is
     * used to provide LuckPerms API data
     *
     * @return LuckPerms API Hook
     */
    LuckPermsHook getLuckPermsHook();

    /**
     * Gets EconomyFactory
     *
     * @return EconomyFactory
     */
    EconomyFactory getEconomyFactory();

    /**
     * Gets default player bank economy
     *
     * @return player bank economy
     */
    Economy getPlayerBankEconomy();

    /**
     * Gets default player cash economy
     *
     * @return player cash economy
     */
    Economy getPlayerCashEconomy();

    /**
     *
     * @return
     */
    PacketService getPacketService();

    /**
     *
     * @return
     */
    ServerManager getServerManager();

    /**
     *
     * @return
     */
    UserManager getUserManager();

    /**
     *
     * @param plugin
     */
    void unload(Plugin plugin);
}
