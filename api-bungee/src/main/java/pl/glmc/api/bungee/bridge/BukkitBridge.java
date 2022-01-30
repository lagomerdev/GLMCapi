package pl.glmc.api.bungee.bridge;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public interface BukkitBridge {

    /**
     *
     * @param player
     * @param sound
     */
    public void playSound(ProxiedPlayer player, String sound, float volume, float pitch);
}
