package pl.glmc.api.bungee;

import net.md_5.bungee.api.plugin.Plugin;
import pl.glmc.api.bungee.bridge.BukkitBridge;
import pl.glmc.api.bungee.packet.PacketService;
import pl.glmc.api.bungee.server.ServerManager;
import pl.glmc.api.bungee.user.UserManager;
import pl.glmc.api.common.LuckPermsHook;

public interface GlmcApiBungee {

    /**
     * Gets LuckPermsHook instance which is
     * used to provide LuckPerms API data
     *
     * @return LuckPerms API Hook
     */
    LuckPermsHook getLuckPermsHook();

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
     * @return
     */
    BukkitBridge getBukkitBridge();

    /**
     *
     * @param plugin
     */
    void unload(Plugin plugin);
}
