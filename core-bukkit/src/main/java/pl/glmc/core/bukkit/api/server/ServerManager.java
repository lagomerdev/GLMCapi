package pl.glmc.core.bukkit.api.server;

import net.md_5.bungee.api.ChatColor;
import pl.glmc.api.common.server.Server;
import pl.glmc.core.bukkit.GlmcCoreBukkit;
import pl.glmc.core.common.packets.server.ServerRegistrationRequest;

public class ServerManager {
    private final GlmcCoreBukkit plugin;
    private final ServerRegistrationHandler registrationHandler;

    public ServerManager(final GlmcCoreBukkit plugin) {
        this.plugin = plugin;

        this.registrationHandler = new ServerRegistrationHandler(this.plugin);

        this.registerServer();
    }

    private void registerServer() {
        Server server = new Server(this.plugin.getServerId());
        ServerRegistrationRequest request = new ServerRegistrationRequest(server);

        this.registrationHandler.create(request.getUniqueId())
                .thenAccept(response -> {
                    if (response.isSuccess()) {
                        this.plugin.getLogger().info(ChatColor.GREEN + "Server has been successfully registered!");
                    } else {
                        this.plugin.getLogger().info(ChatColor.RED + "Server registration has been declined! Stopping server...");
                        //this.plugin.getServer().shutdown();
                    }
                });

        this.plugin.getApiProvider().getPacketService().sendPacket(request, "proxy");

    }
}
