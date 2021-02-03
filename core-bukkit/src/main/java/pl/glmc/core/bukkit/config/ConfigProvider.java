package pl.glmc.core.bukkit.config;

import net.md_5.bungee.api.ChatColor;
import pl.glmc.api.common.config.DatabaseConfig;
import pl.glmc.api.common.config.RedisConfig;
import pl.glmc.core.bukkit.GlmcCoreBukkit;

public class ConfigProvider {
    private final GlmcCoreBukkit plugin;

    private ConfigData configData;
    private DatabaseConfig databaseConfig;
    private RedisConfig redisConfig;

    public ConfigProvider(GlmcCoreBukkit plugin) {
        this.plugin = plugin;

        try {
            this.loadConfig();
            this.loadDatabaseConfig();
            this.loadRedisConfig();
        } catch (NullPointerException exception) {
            exception.printStackTrace();

            this.plugin.getLogger().warning(ChatColor.RED + "Failed to load config!");
        }
    }

    private void loadDatabaseConfig() {
        final String host = this.plugin.getConfig().getString("connections.mysql.host");
        final String database = this.plugin.getConfig().getString("connections.mysql.database");
        final String username = this.plugin.getConfig().getString("connections.mysql.user");
        final String password = this.plugin.getConfig().getString("connections.mysql.password");
        final int port = this.plugin.getConfig().getInt("connections.mysql.port");
        final int maxPoolSize = this.plugin.getConfig().getInt("connections.mysql.max_pool_size");

        this.databaseConfig = new DatabaseConfig(host, database, username, password, port, maxPoolSize);
    }

    private void loadRedisConfig() {
        final String host = this.plugin.getConfig().getString("connections.redis.host");
        final String password = this.plugin.getConfig().getString("connections.redis.password");
        final int port = this.plugin.getConfig().getInt("connections.redis.port");
        final int timeout = this.plugin.getConfig().getInt("connections.redis.timeout");

        this.redisConfig = new RedisConfig(host, password, port, timeout);
    }

    private void loadConfig() {
        this.configData = new ConfigData();
    }

    public ConfigData getConfigData() {
        return configData;
    }

    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }

    public RedisConfig getRedisConfig() {
        return redisConfig;
    }
}
