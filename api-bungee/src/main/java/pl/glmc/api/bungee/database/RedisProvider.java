package pl.glmc.api.bungee.database;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import org.apache.commons.lang.StringUtils;
import pl.glmc.api.common.config.RedisConfig;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.ArrayList;
import java.util.List;

/**
 * Redis API
 */
public class RedisProvider {

    private final Plugin plugin;

    private RedisConfig redisConfig;

    private JedisPool jedisPool;

    private final List<JedisPubSub> jedisPubSubList;
    private final List<BinaryJedisPubSub> binaryJedisPubSubList;

    /**
     *
     * @param plugin instance of a plugin
     * @param redisConfig config with redis credentials
     */
    public RedisProvider(final Plugin plugin, RedisConfig redisConfig) {
        this.plugin = plugin;
        this.redisConfig = redisConfig;

        this.jedisPubSubList = new ArrayList<>();
        this.binaryJedisPubSubList = new ArrayList<>();

        this.load();
    }

    /**
     * Loads redis connection pool
     */
    public void load() {
        try {
            final JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(100);
            jedisPoolConfig.setMaxIdle(50);

            if (StringUtils.isBlank(this.redisConfig.getPassword()) || StringUtils.isEmpty(this.redisConfig.getPassword())) {
                this.jedisPool = new JedisPool(jedisPoolConfig, this.redisConfig.getHost(), this.redisConfig.getPort(), this.redisConfig.getTimeout());
            } else {
                this.jedisPool = new JedisPool(jedisPoolConfig, this.redisConfig.getHost(), this.redisConfig.getPort(), this.redisConfig.getTimeout(), this.redisConfig.getPassword());
            }
            this.plugin.getLogger().info(ChatColor.GREEN + "Successfully connected to REDIS!");
        } catch (JedisConnectionException exception) {
            exception.printStackTrace();

            this.plugin.getLogger().warning(ChatColor.RED + "An error occurred while trying to connect to REDIS!");
            this.plugin.getLogger().warning(ChatColor.RED + "Make sure that redis credentials are set up correctly in config.yml!");
        }
    }

    /**
     * Closes subscribed listeners and redis connection pool
     */
    public void unload() {
        for (BinaryJedisPubSub binaryJedisPubSub : this.binaryJedisPubSubList) {
            binaryJedisPubSub.unsubscribe();
            binaryJedisPubSub.punsubscribe();
        }
        for (JedisPubSub jedisPubSub : this.jedisPubSubList) {
            jedisPubSub.unsubscribe();
            jedisPubSub.punsubscribe();
        }

        this.jedisPool.destroy();
        this.jedisPool.close();
    }

    /**
     * Registers a JedisPubSub listener asynchronously
     *
     * @param jedisPubSub instance of a listener
     * @param channels subscribed channels
     */
    public void subscribe(final JedisPubSub jedisPubSub, final String... channels) {
        this.plugin.getProxy().getScheduler().runAsync(this.plugin, () -> {
            try (Jedis jedis = this.jedisPool.getResource()) {
                this.jedisPubSubList.add(jedisPubSub);

                jedis.subscribe(jedisPubSub, channels);
            }
        });
    }

    /**
     * Registers a BinaryJedisPubSub listener asynchronously
     *
     * @param binaryJedisPubSub instance of a listener
     * @param channels subscribed channels
     */
    public void subscribe(final BinaryJedisPubSub binaryJedisPubSub, final byte... channels) {
        this.plugin.getProxy().getScheduler().runAsync(this.plugin, () -> {
            try (Jedis jedis = this.jedisPool.getResource()) {
                this.binaryJedisPubSubList.add(binaryJedisPubSub);

                jedis.psubscribe(binaryJedisPubSub, channels);
            }
        });
    }

    /**
     * Registers a JedisPubSub listener asynchronously
     *
     * @param jedisPubSub instance of a listener
     * @param patterns subscribed patterns
     */
    public void psubscribe(final JedisPubSub jedisPubSub, final String... patterns) {
        this.plugin.getProxy().getScheduler().runAsync(this.plugin, () -> {
            try (Jedis jedis = this.jedisPool.getResource()) {
                this.jedisPubSubList.add(jedisPubSub);

                jedis.psubscribe(jedisPubSub, patterns);
            }
        });
    }

    /**
     * Registers a BinaryJedisPubSub listener asynchronously
     *
     * @param binaryJedisPubSub instance of a listener
     * @param patterns subscribed patterns
     */
    public void psubscribe(final BinaryJedisPubSub binaryJedisPubSub, final byte... patterns) {
        this.plugin.getProxy().getScheduler().runAsync(this.plugin, () -> {
            try (Jedis jedis = this.jedisPool.getResource()) {
                this.binaryJedisPubSubList.add(binaryJedisPubSub);

                jedis.psubscribe(binaryJedisPubSub, patterns);
            }
        });
    }

    /**
     * Publishes redis string message
     *
     * @param channel redis channel
     * @param message string message to publish
     */
    public void publish(final String channel, final String message) {
        this.plugin.getProxy().getScheduler().runAsync(this.plugin, () -> {
            try (Jedis jedis = this.jedisPool.getResource()) {
                jedis.publish(channel, message);
            }
        });
    }

    /**
     * Publishes redis binary message
     *
     * @param channel redis channel
     * @param message byte[] message to publish
     */
    public void publish(final byte[] channel, final byte[] message) {
        this.plugin.getProxy().getScheduler().runAsync(this.plugin, () -> {
            try (Jedis jedis = this.jedisPool.getResource()) {
                jedis.publish(channel, message);
            }
        });
    }

    /**
     *
     * @return redis connection pool
     */
    public JedisPool getJedisPool() {
        return this.jedisPool;
    }
}
