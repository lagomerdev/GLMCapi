package pl.glmc.core.bukkit.api.economy;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.glmc.api.common.economy.Economy;

import java.util.UUID;

public class LocalCacheListener implements Listener {
    private final Economy apiEconomyProvider;
    private final LocalEconomy localEconomy;

    public LocalCacheListener(Economy economy, LocalEconomy localEconomy) {
        this.apiEconomyProvider = economy;
        this.localEconomy = localEconomy;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent joinEvent) {
        UUID playerUUID = joinEvent.getPlayer().getUniqueId();
        if (!this.apiEconomyProvider.isCached(playerUUID)) {
            this.apiEconomyProvider.cacheAccount(playerUUID);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent quitEvent) {
        UUID playerUUID = quitEvent.getPlayer().getUniqueId();
        if (this.apiEconomyProvider.isCached(playerUUID)) {
            this.apiEconomyProvider.removeFromCache(playerUUID);
        }
    }
}
