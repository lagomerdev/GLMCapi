package pl.glmc.core.bungee.api.packet;

import org.apache.commons.lang3.StringUtils;
import pl.glmc.core.bungee.GlmcCoreBungee;
import redis.clients.jedis.JedisPubSub;

public class ApiNetworkService extends JedisPubSub {
    private final GlmcCoreBungee plugin;
    private final ApiPacketService packetService;

    private final String baseChannel;

    public ApiNetworkService(final GlmcCoreBungee plugin, final ApiPacketService packetService) {
        this.plugin = plugin;
        this.packetService = packetService;

        this.baseChannel = "proxy.";

        this.plugin.getRedisProvider().psubscribe(this, this.baseChannel + "*");
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
        //System.out.println(ChatColor.BLUE + "Received " + System.currentTimeMillis());

        String packetChannel = StringUtils.replaceOnce(channel, this.baseChannel, "");

        this.packetService.packetReceived(packetChannel, message);
    }
}
