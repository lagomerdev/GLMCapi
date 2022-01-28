package pl.glmc.api.bungee.database;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import org.apache.commons.lang3.StringUtils;
import pl.glmc.api.common.Callback;
import pl.glmc.api.common.config.RedisConfig;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

                jedis.psubscribe(jedisPubSub, channels);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(ChatColor.RED + "Uncatched exception occureed in " + jedisPubSub.getClass().getName() + "! Re-subscribing JedisPubSub...");

                jedisPubSub.unsubscribe();

                this.jedisPubSubList.remove(jedisPubSub);

                this.psubscribe(jedisPubSub, channels);
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

                jedis.subscribe(binaryJedisPubSub, channels);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(ChatColor.RED + "Uncatched exception occureed in " + binaryJedisPubSub.getClass().getName() + "! Re-subscribing BinaryJedisPubSub...");

                binaryJedisPubSub.unsubscribe();

                this.binaryJedisPubSubList.remove(binaryJedisPubSub);

                this.psubscribe(binaryJedisPubSub, channels);
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
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(ChatColor.RED + "Uncatched exception occureed in " + jedisPubSub.getClass().getName() + "! Re-subscribing JedisPubSub...");

                jedisPubSub.unsubscribe();

                this.jedisPubSubList.remove(jedisPubSub);

                this.psubscribe(jedisPubSub, patterns);
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
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(ChatColor.RED + "Uncatched exception occureed in " + binaryJedisPubSub.getClass().getName() + "! Re-subscribing BinaryJedisPubSub...");

                binaryJedisPubSub.unsubscribe();

                this.binaryJedisPubSubList.remove(binaryJedisPubSub);

                this.psubscribe(binaryJedisPubSub, patterns);
            }
        });
    }


    /**
     * Synchronously publishes redis string message
     *
     * @param channel redis channel
     * @param message string message to publish
     */
    public void publish(final String channel, final String message) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.publish(channel, message);
        }
    }

    /**
     * Synchronously publishes redis binary message
     *
     * @param channel redis channel
     * @param message byte[] message to publish
     */
    public void publish(final byte[] channel, final byte[] message) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.publish(channel, message);
        }
    }

    /**
     * Asynchronously publishes redis string message
     *
     * @param channel redis channel
     * @param message string message to publish
     */
    public void publishAsync(final String channel, final String message) {
        this.plugin.getProxy().getScheduler().runAsync(this.plugin, () -> {
            try (Jedis jedis = this.jedisPool.getResource()) {
                jedis.publish(channel, message);
            }
        });
    }

    /**
     * Asynchronously publishes redis binary message
     *
     * @param channel redis channel
     * @param message byte[] message to publish
     */
    public void publishAsync(final byte[] channel, final byte[] message) {
        this.plugin.getProxy().getScheduler().runAsync(this.plugin, () -> {
            try (Jedis jedis = this.jedisPool.getResource()) {
                jedis.publish(channel, message);
            }
        });
    }

    /**
     * Synchronously sets key to given value
     *
     * @param key redis key
     * @param value value to set
     */
    public void set(final String key, final String value) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.set(key, value);
        }
    }

    /**
     * Asynchronously sets key to given value
     *
     * @param key redis key
     * @param value value to set
     */
    public void setAsync(final String key, final String value) {
        this.plugin.getProxy().getScheduler().runAsync(this.plugin, () -> {
            try (Jedis jedis = this.jedisPool.getResource()) {
                jedis.set(key, value);
            }
        });
    }

    /**
     * Synchronously sets map's field to given value
     *
     * @param key map's key
     * @param field map's field
     * @param value value to set
     */
    public void hset(final String key, final String field, final String value) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.hset(key, field, value);
        }
    }

    /**
     * Asynchronously sets map's field to given value
     *
     * @param key map's key
     * @param field map's field
     * @param value value to set
     */
    public void hsetAsync(final String key, final String field, final String value) {
        this.plugin.getProxy().getScheduler().runAsync(this.plugin, () -> {
            try (Jedis jedis = this.jedisPool.getResource()) {
                jedis.hset(key, field, value);
            }
        });
    }

    /**
     * Synchronously deletes map's given fields
     *
     * @param key map's key
     * @param field fields to remove
     */
    public void hdel(final String key, final String... field) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.hdel(key, field);
        }
    }

    /**
     * Asynchronously deletes map's given fields
     *
     * @param key map's key
     * @param field fields to remove
     */
    public void hdelAsync(final String key, final String... field) {
        this.plugin.getProxy().getScheduler().runAsync(this.plugin, () -> {
            try (Jedis jedis = this.jedisPool.getResource()) {
                jedis.hdel(key, field);
            }
        });
    }

    /**
     * Synchronously gets value of given field in map
     *
     * @param key map's key
     * @param field value's field
     * @return value of given field
     */
    public String hget(final String key, final String field) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.hget(key, field);
        }
    }

    /**
     * Asynchronously gets value of given field in map
     *
     * @param callback callback
     * @param key map's key
     * @param field value's field
     */
    public void hgetAsync(final Callback<String> callback, final String key, final String field) {
        this.plugin.getProxy().getScheduler().runAsync(this.plugin, () -> {
            try (Jedis jedis = this.jedisPool.getResource()) {
                callback.done(jedis.hget(key, field));
            }
        });
    }

    /**
     * Synchronously gets map with fields and values of given key
     *
     * @param key map's key
     * @return map with fields and their values assigned to given key
     */
    public Map<String, String> hgetAll(final String key) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.hgetAll(key);
        }
    }

    /**
     * Synchronously gets map with fields and values of given key
     *
     * @param callback callback
     * @param key map's key
     */
    public void hgetAllAsync(final Callback<Map<String, String>> callback, final String key) {
        this.plugin.getProxy().getScheduler().runAsync(this.plugin, () -> {
            try (Jedis jedis = this.jedisPool.getResource()) {
                callback.done(jedis.hgetAll(key));
            }
        });
    }

    /**
     * Synchronously checks if field exists in map with given key
     *
     * @param key map's key
     * @param field field to check
     * @return true if field exists false if not
     */
    public boolean hexists(final String key, final String field) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.hexists(key, field);
        }
    }

    /**
     * Asynchronously checks if field exists in map with given key
     *
     * @param callback callback
     * @param key map's key
     * @param field field to check
     */
    public void hexistsAsync(final Callback<Boolean> callback, final String key, final String field) {
        this.plugin.getProxy().getScheduler().runAsync(this.plugin, () -> {
            try (Jedis jedis = this.jedisPool.getResource()) {
                callback.done(jedis.hexists(key, field));
            }
        });
    }

    /**
     * Gets value of given key synchronously
     *
     * @param key redis key
     * @return string value
     */
    public String getSync(final String key) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.get(key);
        }
    }

    /**
     * Gets value of given key asynchronously
     *
     * @param callback callback called when getting value is completed
     * @param key redis key
     */
    public void getAsync(final Callback<String> callback, final String key) {
        this.plugin.getProxy().getScheduler().runAsync(this.plugin, () -> {
            try (Jedis jedis = this.jedisPool.getResource()) {
                String result = jedis.get(key);

                callback.done(result);
            }
        });
    }

    /**
     * Gets Jedis Pool
     *
     * @return redis connection pool
     */
    public JedisPool getJedisPool() {
        return this.jedisPool;
    }
}
