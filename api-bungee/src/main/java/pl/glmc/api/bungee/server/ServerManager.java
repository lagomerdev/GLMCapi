package pl.glmc.api.bungee.server;

import pl.glmc.api.common.server.Server;

import java.util.Collection;

public interface ServerManager {
    /**
     *
     * @return
     */
    Collection<Server> getRegisteredServers();
}
