package pl.glmc.core.bungee.api.bridge;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import pl.glmc.api.bungee.bridge.BukkitBridge;
import pl.glmc.core.bungee.GlmcCoreBungee;
import pl.glmc.core.common.packets.bridge.PlaySound;

public class ApiBukkitBridge implements BukkitBridge {

    private final GlmcCoreBungee plugin;

    public ApiBukkitBridge(GlmcCoreBungee plugin) {
        this.plugin = plugin;
    }

    @Override
    public void playSound(ProxiedPlayer player, String sound, float volume, float pitch) {
        PlaySound playSound = new PlaySound(player.getUniqueId(), sound, volume, pitch);

        this.plugin.getApiProvider().getPacketService().sendPacket(playSound, player.getServer().getInfo().getName());
    }
}
