/*package pl.glmc.core.bukkit.api;

import pl.glmc.core.bungee.api.economy.ApiEconomyProvider;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RefreshBalanceTask implements Runnable {

    private final ApiEconomyProvider apiEconomyProvider;
    private final UUID accountUUID;

    public RefreshBalanceTask(ApiEconomyProvider apiEconomyProvider, UUID accountUUID) {
        this.apiEconomyProvider = apiEconomyProvider;
        this.accountUUID = accountUUID;
    }

    @Override
    public void run() {
        CompletableFuture<Void> blockingFuture = new CompletableFuture<>();

        this.apiEconomyProvider.getAndUpdate(blockingFuture, this.accountUUID);

        try {
            blockingFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}*/