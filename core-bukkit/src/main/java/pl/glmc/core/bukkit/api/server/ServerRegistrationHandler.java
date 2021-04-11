package pl.glmc.core.bukkit.api.server;

import pl.glmc.api.common.packet.listener.ResponseHandlerListener;
import pl.glmc.core.bukkit.GlmcCoreBukkit;
import pl.glmc.core.common.packets.LocalPacketRegistry;
import pl.glmc.core.common.packets.server.ServerRegistrationResponse;

public class ServerRegistrationHandler extends ResponseHandlerListener<ServerRegistrationResponse, ServerRegistrationResponse> {
    final GlmcCoreBukkit plugin;

    public ServerRegistrationHandler(final GlmcCoreBukkit plugin) {
        super(LocalPacketRegistry.SERVER.REGISTRATION_RESPONSE, ServerRegistrationResponse.class);

        this.plugin = plugin;

        this.plugin.getApiProvider().getPacketService().registerListener(this);
    }

    @Override
    public void received(ServerRegistrationResponse packet) {
        this.complete(packet.getOriginUniqueId(), packet);
    }
}
