package pl.glmc.core.bungee;

import com.google.gson.Gson;
import net.md_5.bungee.api.plugin.Plugin;
import pl.glmc.api.bungee.database.DatabaseProvider;
import pl.glmc.api.bungee.database.RedisProvider;
import pl.glmc.core.bungee.api.GlmcApiProvider;
import pl.glmc.core.bungee.config.ConfigProvider;

public class GlmcCoreBungee extends Plugin {

    private ConfigProvider configProvider;
    private DatabaseProvider databaseProvider;
    private RedisProvider redisProvider;

    private Gson gson;

    @Override
    public void onLoad() {
        this.configProvider = new ConfigProvider(this);
        this.databaseProvider = new DatabaseProvider(this, this.configProvider.getDatabaseConfig());
        this.redisProvider = new RedisProvider(this, this.configProvider.getRedisConfig());

        this.gson = new Gson();
    }

    @Override
    public void onEnable() {
        GlmcApiProvider glmcApiProvider = new GlmcApiProvider(this);
    }

    @Override
    public void onDisable() {
        this.redisProvider.unload();
        this.databaseProvider.unload();
    }

    public ConfigProvider getConfigProvider() {
        return configProvider;
    }

    public DatabaseProvider getDatabaseProvider() {
        return databaseProvider;
    }

    public RedisProvider getRedisProvider() {
        return redisProvider;
    }

    public Gson getGson() {
        return gson;
    }
}
