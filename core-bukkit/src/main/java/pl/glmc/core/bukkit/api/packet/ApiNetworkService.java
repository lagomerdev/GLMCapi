package pl.glmc.core.bukkit.api.packet;

import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import pl.glmc.core.bukkit.GlmcCoreBukkit;
import redis.clients.jedis.JedisPubSub;

public class ApiNetworkService extends JedisPubSub {
    private final GlmcCoreBukkit plugin;
    private final ApiPacketService packetService;

    private String baseChannel;

    public ApiNetworkService(final GlmcCoreBukkit plugin, final ApiPacketService packetService) {
        this.plugin = plugin;
        this.packetService = packetService;

        this.baseChannel = this.plugin.getConfigProvider().getConfigData().getServerId() + ".";

        this.plugin.getRedisProvider().psubscribe(this, this.baseChannel + "*", "all.*");
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
        //System.out.println(ChatColor.BLUE + "Received " + System.currentTimeMillis());

        String rawPattern = StringUtils.replace(pattern, "*", "");
        String packetChannel = StringUtils.replace(channel, rawPattern, "");

        this.plugin.getLogger().info(ChatColor.YELLOW + "Raw pattern: " + ChatColor.GOLD + rawPattern);

        this.packetService.packetReceived(packetChannel, message);
    }
}
