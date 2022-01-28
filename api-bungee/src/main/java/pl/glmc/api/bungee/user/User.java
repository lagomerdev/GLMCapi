package pl.glmc.api.bungee.user;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.Timestamp;
import java.util.UUID;

public interface User {

    /** nullable
     *
     * @return
     */
    ProxiedPlayer getPlayer();

    /**
     *
     * @return
     */
    UUID getUniqueId();

    /**
     *
     * @return
     */
    boolean isOnline();

    /**
     *
     */
    void sendMessage(TextComponent message);

    /**
     *
     * @return
     */
    CachedUserData getCachedData();

    /**
     *
     */
    void updateCachedData();
}
