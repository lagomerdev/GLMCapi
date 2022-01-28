package pl.glmc.core.bukkit.api.economy.listener;

import pl.glmc.api.common.packet.listener.ResponseHandlerListener;
import pl.glmc.core.bukkit.GlmcCoreBukkit;
import pl.glmc.core.common.packets.LocalPacketRegistry;
import pl.glmc.core.common.packets.economy.EconomyRegistrationResponse;

public class EconomyRegistrationHandler extends ResponseHandlerListener<EconomyRegistrationResponse, Boolean> {
    private final GlmcCoreBukkit plugin;

    public EconomyRegistrationHandler(final GlmcCoreBukkit plugin) {
        super(LocalPacketRegistry.ECONOMY.REGISTRATION_RESPONSE, EconomyRegistrationResponse.class);

        this.plugin = plugin;

        this.plugin.getApiProvider().getPacketService().registerListener(this, this.plugin);
    }

    @Override
    public void received(EconomyRegistrationResponse packet) {
        this.complete(packet.getOriginUniqueId(), packet.isSuccess());
    }
}
