package pl.glmc.core.common.packets.economy;

import pl.glmc.api.common.config.EconomyConfig;
import pl.glmc.api.common.packet.InfoPacket;
import pl.glmc.api.common.packet.PacketInfo;
import pl.glmc.core.common.packets.LocalPacketRegistry;

public class EconomyRegistered extends InfoPacket {
    private final static PacketInfo PACKET_INFO = LocalPacketRegistry.ECONOMY.REGISTERED;
    private final EconomyConfig economyConfig;

    public EconomyRegistered(final EconomyConfig economyConfig) {
        this.economyConfig = economyConfig;
    }

    public EconomyConfig getEconomyConfig() {
        return this.economyConfig;
    }

    @Override
    public String getPacketId() {
        return PACKET_INFO.getId();
    }
}
