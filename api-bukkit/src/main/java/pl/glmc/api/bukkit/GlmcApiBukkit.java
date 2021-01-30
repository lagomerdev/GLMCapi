package pl.glmc.api.bukkit;

import pl.glmc.api.common.LuckPermsHook;

public interface GlmcApiBukkit {

    /**
     * Gets LuckPermsHook instance which is
     * used to provide LuckPerms API data
     *
     * @return LuckPerms API Hook
     */
    LuckPermsHook getLuckPermsHook();
}
