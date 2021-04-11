package pl.glmc.core.bungee.api.server;

import pl.glmc.api.bungee.server.ServerManager;
import pl.glmc.api.common.server.Server;
import pl.glmc.core.bungee.GlmcCoreBungee;
import pl.glmc.core.bungee.api.GlmcApiProvider;
import pl.glmc.core.bungee.api.packet.ApiPacketService;

import java.util.Collection;
import java.util.HashMap;

public class ApiServerManager implements ServerManager {
    private final GlmcCoreBungee plugin;
    private final HashMap<String, Server> registeredServers;

    public ApiServerManager(final GlmcCoreBungee pluign) {
        this.plugin = pluign;

        this.registeredServers = new HashMap<>();

        ServerRegistrationListener serverRegistrationListener = new ServerRegistrationListener(this.plugin, this);
    }

    public boolean registerServer(Server server) {
        if (this.registeredServers.containsKey(server.getServerId())) {
            return false;
        }

        this.registeredServers.put(server.getServerId(), server);

        return true;
    }

    @Override
    public Collection<Server> getRegisteredServers() {
        return this.registeredServers.values();
    }
}
