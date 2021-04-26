package pl.glmc.core.bungee.api.user.data;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pl.glmc.api.bungee.user.CachedUserData;
import pl.glmc.api.bungee.user.User;
import pl.glmc.core.bungee.GlmcCoreBungee;

import java.lang.ref.WeakReference;
import java.util.UUID;

public class ApiUser implements User {
    private final WeakReference<ProxiedPlayer> playerReference;
    private final GlmcCoreBungee plugin;
    private final CachedUserData cachedUserData;
    private final UUID playerUniqueId;

    public ApiUser(GlmcCoreBungee plugin, ProxiedPlayer player, CachedUserData cachedUserData) {
        this.plugin = plugin;
        this.playerReference = new WeakReference<>(player);

        this.cachedUserData = cachedUserData;

        this.playerUniqueId = player.getUniqueId();
    }

    @Override
    public ProxiedPlayer getPlayer() {
        return this.playerReference.get();
    }

    @Override
    public UUID getUniqueId() {
        return this.playerUniqueId;
    }

    @Override
    public boolean isOnline() {
        ProxiedPlayer player = this.getPlayer();

        return player != null && player.isConnected();
    }

    @Override
    public void sendMessage(TextComponent message) {
        ProxiedPlayer player = this.getPlayer();

        if (player != null) {
            player.sendMessage(message);
        } else {
            throw new NullPointerException("User is not connected!");
        }
    }

    @Override
    public CachedUserData getCachedData() {
        return this.cachedUserData;
    }

    @Override
    public void updateCachedData() {
        String jsonData = this.plugin.getGson().toJson(this);

        this.plugin.getRedisProvider().hsetAsync("api.users", this.playerUniqueId.toString(), jsonData);
    }
}
