package pl.glmc.core.bungee.api;

import net.md_5.bungee.api.ChatColor;
import pl.glmc.api.bungee.GlmcApiBungee;
import pl.glmc.api.bungee.GlmcApiBungeeProvider;
import pl.glmc.api.common.LuckPermsHook;
import pl.glmc.core.bungee.GlmcCoreBungee;
import pl.glmc.core.bungee.api.hook.ApiLuckPermsHook;

public class GlmcApiProvider implements GlmcApiBungee {

    private final GlmcCoreBungee plugin;

    private final LuckPermsHook luckPermsHook;

    public GlmcApiProvider(GlmcCoreBungee plugin) {
        this.plugin = plugin;

        this.luckPermsHook = new ApiLuckPermsHook(this.plugin);

        GlmcApiBungeeProvider.register(this);

        this.plugin.getLogger().info(ChatColor.DARK_GREEN + "Loaded API Provider");
    }

    @Override
    public LuckPermsHook getLuckPermsHook() {
        return this.luckPermsHook;
    }
}
