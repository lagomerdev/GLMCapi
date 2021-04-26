package pl.glmc.api.bukkit.user;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface User {

    /** nullable
     *
     * @return
     */
    Player getPlayer();

    /**
     *
     * @return
     */
    UUID getUniqueId();

    /**
     *
     */
    void sendMessage(TextComponent message);
}
