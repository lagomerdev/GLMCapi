package pl.glmc.api.common.server;

public class Server {
    private final String serverId;

    public Server(final String serverId) {
        this.serverId = serverId;
    }

    public String getServerId() {
        return serverId;
    }
}
