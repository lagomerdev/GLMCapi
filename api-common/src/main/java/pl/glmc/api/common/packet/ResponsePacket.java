package pl.glmc.api.common.packet;

import java.util.UUID;

public abstract class ResponsePacket implements Packet {
    protected final boolean success;
    protected final UUID originUniqueId;

    private String sender;

    public ResponsePacket(final boolean success, final UUID originUniqueId) {
        this.success = success;
        this.originUniqueId = originUniqueId;
    }

    /**
     *
     * @return
     */
    public boolean isSuccess() {
        return this.success;
    }

    /**
     *
     * @return
     */
    public UUID getOriginUniqueId() {
        return originUniqueId;
    }

    @Override
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     *
     * @return
     */
    @Override
    public String getSender() {
        return this.sender;
    }
}
