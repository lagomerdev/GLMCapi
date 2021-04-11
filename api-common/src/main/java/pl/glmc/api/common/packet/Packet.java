package pl.glmc.api.common.packet;

import java.util.UUID;

public interface Packet {

    /**
     *
     * @return
     */
    String getPacketId();

    /**
     *
     * @return
     */
    String getSender();

    /**
     *
     */
    void setSender(final String sender);
}