package pl.glmc.api.common.packet;

import java.util.UUID;

public abstract class RequestPacket implements Packet {
    protected final UUID uuid;

    private String sender;

    public RequestPacket() {
        this.uuid = UUID.randomUUID();
    }

    @Override
    public void setSender(String sender) {
        this.sender = sender;
    }

    @Override
    public String getSender() {
        return this.sender;
    }

    /**
     *
     * @return
     */
    public UUID getUniqueId() {
        return this.uuid;
    }
}
