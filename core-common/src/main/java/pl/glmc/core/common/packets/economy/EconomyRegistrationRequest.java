package pl.glmc.core.common.packets.economy;

import pl.glmc.api.common.config.EconomyConfig;
import pl.glmc.api.common.packet.PacketInfo;
import pl.glmc.core.common.packets.LocalPacketRegistry;
import pl.glmc.api.common.packet.RequestPacket;

public class EconomyRegistrationRequest extends RequestPacket {
    private final static PacketInfo PACKET_INFO = LocalPacketRegistry.ECONOMY.REGISTRATION_REQUEST;

    private final EconomyConfig economyConfig;

    public EconomyRegistrationRequest(final EconomyConfig economyConfig) {
        this.economyConfig = economyConfig;
    }

    public EconomyConfig getEconomyConfig() {
        return economyConfig;
    }

    @Override
    public String getPacketId() {
        return PACKET_INFO.getId();
    }
}