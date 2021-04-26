package pl.glmc.core.bungee.api.user;

import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import pl.glmc.core.bungee.GlmcCoreBungee;

public class ApiUserListener implements Listener {

    private final GlmcCoreBungee plugin;
    private final ApiUserManager apiUserManager;

    public ApiUserListener(GlmcCoreBungee plugin, ApiUserManager apiUserManager) {
        this.plugin = plugin;
        this.apiUserManager = apiUserManager;

        this.plugin.getProxy().getPluginManager().registerListener(this.plugin, this);
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onAsyncLogin(LoginEvent loginEvent) {
        if (loginEvent.isCancelled()) {
            return;
        }

        loginEvent.registerIntent(this.plugin);

        this.apiUserManager.processLogin(loginEvent.getConnection());

        loginEvent.completeIntent(this.plugin);
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPostJoin(PostLoginEvent postLoginEvent) {
        this.apiUserManager.processJoin(postLoginEvent.getPlayer());
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onDisconnect(PlayerDisconnectEvent disconnectEvent) {
        this.apiUserManager.processDisconnect(disconnectEvent.getPlayer().getUniqueId());
    }
}
