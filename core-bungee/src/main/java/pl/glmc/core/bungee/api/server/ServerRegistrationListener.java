package pl.glmc.core.bungee.api.server;

import pl.glmc.api.common.packet.listener.PacketListener;
import pl.glmc.core.bungee.GlmcCoreBungee;
import pl.glmc.core.bungee.api.GlmcApiProvider;
import pl.glmc.core.common.packets.LocalPacketRegistry;
import pl.glmc.core.common.packets.server.ServerRegistrationRequest;
import pl.glmc.core.common.packets.server.ServerRegistrationResponse;

public class ServerRegistrationListener extends PacketListener<ServerRegistrationRequest> {
    private final GlmcCoreBungee plugin;
    private final ApiServerManager serverManager;

    public ServerRegistrationListener(final GlmcCoreBungee plugin, final ApiServerManager serverManager) {
        super(LocalPacketRegistry.SERVER.REGISTRATION_REQUEST, ServerRegistrationRequest.class);

        this.plugin = plugin;
        this.serverManager = serverManager;

        this.plugin.getApiProvider().getPacketService().registerListener(this, this.plugin);
    }

    @Override
    public void received(ServerRegistrationRequest packet) {
        boolean success = this.serverManager.registerServer(packet.getServer());

        ServerRegistrationResponse response;
        if (success) {
            response = new ServerRegistrationResponse(true, packet.getUniqueId(), this.plugin.getApiProvider().getEconomyFactory().getRegisteredConfigs());
        } else {
            response = new ServerRegistrationResponse(false, packet.getUniqueId());
        }

        this.plugin.getApiProvider().getPacketService().sendPacket(response, packet.getSender());
    }
}
