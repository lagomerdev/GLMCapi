package pl.glmc.core.common.tracker;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
public class PacketFutureProvider<V> {
    private final ConcurrentHashMap<UUID, CompletableFuture<V>> transferFutureMap = new ConcurrentHashMap<>();

    public CompletableFuture<V> create(UUID id) {
        CompletableFuture<V> packetFuture = new CompletableFuture<>();

        this.transferFutureMap.put(id, packetFuture);

        return packetFuture;
    }

    public boolean complete(UUID id, V value) {
        CompletableFuture<V> packetFuture = this.transferFutureMap.getOrDefault(id, null);

        if (packetFuture == null) {
            return false;
        }

        this.transferFutureMap.remove(id);

        return packetFuture.complete(value);
    }

    public void remove(UUID id) {
        this.transferFutureMap.remove(id);
    }
}