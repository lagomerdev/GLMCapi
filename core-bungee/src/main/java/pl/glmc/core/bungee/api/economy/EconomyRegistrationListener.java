package pl.glmc.core.bungee.api.economy;

import pl.glmc.api.common.packet.listener.PacketListener;
import pl.glmc.core.bungee.GlmcCoreBungee;
import pl.glmc.core.bungee.api.GlmcApiProvider;
import pl.glmc.core.bungee.api.packet.ApiPacketService;
import pl.glmc.core.common.packets.LocalPacketRegistry;
import pl.glmc.core.common.packets.economy.EconomyRegistrationRequest;
import pl.glmc.core.common.packets.economy.EconomyRegistrationResponse;

public class EconomyRegistrationListener extends PacketListener<EconomyRegistrationRequest> {
    private final GlmcCoreBungee plugin;

    public EconomyRegistrationListener(final GlmcCoreBungee plugin) {
        super(LocalPacketRegistry.ECONOMY.REGISTRATION_REQUEST, EconomyRegistrationRequest.class);

        this.plugin = plugin;

        this.plugin.getApiProvider().getPacketService().registerListener(this, this.plugin);
    }

    @Override
    public void received(EconomyRegistrationRequest packet) {
        boolean success;
        try {
            this.plugin.getApiProvider().getEconomyFactory().registerEconomy(packet.getEconomyConfig());

            success = true;
        } catch (Exception e) {
            success = false;
        }

        EconomyRegistrationResponse response = new EconomyRegistrationResponse(success, packet.getUniqueId());
        this.plugin.getApiProvider().getPacketService().sendPacket(response, packet.getSender());
    }
}
