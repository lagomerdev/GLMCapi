package pl.glmc.api.common.packet.listener;

import pl.glmc.api.common.packet.Packet;
import pl.glmc.api.common.packet.PacketInfo;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ResponseHandlerListener<T extends Packet, V> extends PacketListener<T> {
    private final ConcurrentHashMap<UUID, CompletableFuture<V>> awaitingPacketResponses;

    public ResponseHandlerListener(PacketInfo packetInfo, Class<T> packetClass) {
        super(packetInfo, packetClass);

        this.awaitingPacketResponses = new ConcurrentHashMap<>();
    }

    public CompletableFuture<V> create(UUID id) {
        CompletableFuture<V> packetFuture = new CompletableFuture<>();

        this.awaitingPacketResponses.put(id, packetFuture);

        return packetFuture;
    }

    public boolean complete(UUID id, V value) {
        CompletableFuture<V> packetFuture = this.awaitingPacketResponses.getOrDefault(id, null);

        if (packetFuture == null) {
            return false;
        }

        this.awaitingPacketResponses.remove(id);

        return packetFuture.complete(value);
    }

    public void remove(UUID id) {
        this.awaitingPacketResponses.remove(id);
    }
}
