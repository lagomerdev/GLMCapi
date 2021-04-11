package pl.glmc.core.common.packets.server;

import pl.glmc.api.common.config.EconomyConfig;
import pl.glmc.api.common.packet.PacketInfo;
import pl.glmc.core.common.packets.LocalPacketRegistry;
import pl.glmc.api.common.packet.ResponsePacket;

import java.util.HashSet;
import java.util.UUID;

public class ServerRegistrationResponse extends ResponsePacket {
    private final static PacketInfo PACKET_INFO = LocalPacketRegistry.SERVER.REGISTRATION_RESPONSE;

    private final HashSet<EconomyConfig> registeredEconomies;

    public ServerRegistrationResponse(final boolean success, final UUID originUniqueId) {
        super(success, originUniqueId);

        this.registeredEconomies = new HashSet<>();
    }

    public ServerRegistrationResponse(final boolean success, final UUID originUniqueId, final HashSet<EconomyConfig> registeredEconomies) {
        super(success, originUniqueId);

        this.registeredEconomies = registeredEconomies;
    }

    public HashSet<EconomyConfig> getRegisteredEconomies() {
        return this.registeredEconomies;
    }

    @Override
    public String getPacketId() {
        return PACKET_INFO.getId();
    }
}