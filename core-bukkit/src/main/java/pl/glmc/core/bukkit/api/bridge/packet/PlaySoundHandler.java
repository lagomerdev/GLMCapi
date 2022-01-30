package pl.glmc.core.bukkit.api.bridge.packet;

import org.bukkit.Sound;
import pl.glmc.api.common.packet.listener.PacketListener;
import pl.glmc.core.bukkit.GlmcCoreBukkit;
import pl.glmc.core.common.packets.LocalPacketRegistry;
import pl.glmc.core.common.packets.bridge.PlaySound;

public class PlaySoundHandler extends PacketListener<PlaySound> {
    private final GlmcCoreBukkit plugin;

    public PlaySoundHandler(GlmcCoreBukkit plugin) {
        super(LocalPacketRegistry.BRIDGE.PLAY_SOUND, PlaySound.class);
        this.plugin = plugin;

        this.plugin.getApiProvider().getPacketService().registerListener(this, this.plugin);
    }

    @Override
    public void received(PlaySound packet) {
        var player = this.plugin.getServer().getPlayer(packet.getPlayerUniqueId());
        if (player == null || !player.isOnline()) {
            this.plugin.getLogger().warning("Cannot handle PlaySound because player is not online!");

            return;
        }

        player.playSound(player, Sound.valueOf(packet.getSound().toUpperCase()), packet.getVolume(), packet.getPitch());
    }
}
