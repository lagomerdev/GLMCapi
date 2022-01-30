package pl.glmc.api.bukkit;

import org.bukkit.plugin.Plugin;
import pl.glmc.api.bukkit.packet.PacketService;
import pl.glmc.api.bukkit.user.UserManager;
import pl.glmc.api.common.LuckPermsHook;

public interface GlmcApiBukkit {

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
    UserManager getUserManager();

    /**
     *
     * @param plugin
     */
    void unload(Plugin plugin);

    /**
     *
     * @return server id
     */
    String getServerId();
}
