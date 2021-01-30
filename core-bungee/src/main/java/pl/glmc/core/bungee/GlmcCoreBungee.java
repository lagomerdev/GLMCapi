package pl.glmc.core.bungee;

import net.md_5.bungee.api.plugin.Plugin;
import pl.glmc.core.bungee.api.GlmcApiProvider;

public class GlmcCoreBungee extends Plugin {

    @Override
    public void onLoad() {
        //
    }

    @Override
    public void onEnable() {
        GlmcApiProvider glmcApiProvider = new GlmcApiProvider(this);
    }

    @Override
    public void onDisable() {
        //
    }
}
