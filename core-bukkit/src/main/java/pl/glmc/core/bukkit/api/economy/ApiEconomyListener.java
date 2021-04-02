/*package pl.glmc.core.bukkit.api;

import redis.clients.jedis.JedisPubSub;

import java.util.UUID;

public class ApiEconomyListener extends JedisPubSub {

    private final ApiEconomyProvider apiEconomyProvider;

    public ApiEconomyListener(ApiEconomyProvider apiEconomyProvider) {
        this.apiEconomyProvider = apiEconomyProvider;
    }

    @Override
    public void onMessage(String channel, String message) {
        UUID accountUUID = UUID.fromString(message);

        if (this.apiEconomyProvider.isCached(accountUUID) && !this.apiEconomyProvider.checkRefreshIgnored(accountUUID)) {
            this.apiEconomyProvider.refreshCachedData(accountUUID);
        }
    }
}
*/