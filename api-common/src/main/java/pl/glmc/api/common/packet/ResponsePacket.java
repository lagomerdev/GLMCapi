package pl.glmc.api.common.packet;

public abstract class ResponsePacket implements Packet {
    protected final boolean success;

    /**
     *
     * @param success
     */
    public ResponsePacket(final boolean success) {
        this.success = success;
    }

    /**
     *
     * @return
     */
    public boolean isSuccess() {
        return this.success;
    }
}
