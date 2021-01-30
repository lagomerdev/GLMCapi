package pl.glmc.api.bungee;

import pl.glmc.api.common.LuckPermsHook;

public interface GlmcApiBungee {

    /**
     * Gets LuckPermsHook instance which is
     * used to provide LuckPerms API data
     *
     * @return LuckPerms API Hook
     */
    LuckPermsHook getLuckPermsHook();
}
