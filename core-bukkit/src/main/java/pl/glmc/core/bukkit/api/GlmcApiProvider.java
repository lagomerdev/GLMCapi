package pl.glmc.core.bukkit.api;

import pl.glmc.api.bukkit.GlmcApiBukkit;
import pl.glmc.api.common.LuckPermsHook;
import pl.glmc.core.bukkit.GlmcCoreBukkit;
import pl.glmc.core.bukkit.api.hook.ApiLuckPermsHook;

public class GlmcApiProvider implements GlmcApiBukkit {

    private final GlmcCoreBukkit plugin;

    private final LuckPermsHook luckPermsHook;

    public GlmcApiProvider(GlmcCoreBukkit plugin) {
        this.plugin = plugin;

        this.luckPermsHook = new ApiLuckPermsHook(this.plugin);
    }

    @Override
    public LuckPermsHook getLuckPermsHook() {
        return this.luckPermsHook;
    }
}
