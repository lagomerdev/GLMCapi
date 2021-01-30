package pl.glmc.core.bukkit;

import org.bukkit.plugin.java.JavaPlugin;
import pl.glmc.core.bukkit.api.GlmcApiProvider;

public class GlmcCoreBukkit extends JavaPlugin {

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
