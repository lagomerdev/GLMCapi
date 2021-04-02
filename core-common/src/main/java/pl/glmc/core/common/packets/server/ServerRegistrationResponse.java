package pl.glmc.core.common.packets.server;

import pl.glmc.api.common.packet.PacketInfo;
import pl.glmc.core.common.packets.LocalPacketRegistry;
import pl.glmc.api.common.packet.ResponsePacket;

import java.util.ArrayList;
import java.util.List;

public class ServerRegistrationResponse extends ResponsePacket {
    private final static PacketInfo PACKET_INFO = LocalPacketRegistry.SERVER.REGISTRATION_REQUEST;

    private final List<String> registeredEconomies;

    public ServerRegistrationResponse(final boolean success) {
        super(success);

        this.registeredEconomies = new ArrayList<>();
    }

    public ServerRegistrationResponse(final boolean success, final List<String> registeredEconomies) {
        super(success);

        this.registeredEconomies = registeredEconomies;
    }

    public List<String> getRegisteredEconomies() {
        return this.registeredEconomies;
    }

    @Override
    public String getPacketId() {
        return PACKET_INFO.getId();
    }
}