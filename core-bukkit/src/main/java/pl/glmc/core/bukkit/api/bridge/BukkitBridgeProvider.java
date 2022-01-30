package pl.glmc.core.bukkit.api.bridge;

import pl.glmc.core.bukkit.GlmcCoreBukkit;
import pl.glmc.core.bukkit.api.bridge.packet.PlaySoundHandler;

public class BukkitBridgeProvider {

    private final GlmcCoreBukkit plugin;

    public BukkitBridgeProvider(GlmcCoreBukkit plugin) {
        this.plugin = plugin;

        PlaySoundHandler playSoundHandler = new PlaySoundHandler(this.plugin);
    }
}
