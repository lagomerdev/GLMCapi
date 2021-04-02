package pl.glmc.core.bukkit;

import com.google.gson.Gson;
import org.bukkit.plugin.java.JavaPlugin;
import pl.glmc.api.bukkit.database.DatabaseProvider;
import pl.glmc.api.bukkit.database.RedisProvider;
import pl.glmc.core.bukkit.api.GlmcApiProvider;
import pl.glmc.core.bukkit.config.ConfigProvider;

import java.io.File;

public class GlmcCoreBukkit extends JavaPlugin {
    private ConfigProvider configProvider;
    private DatabaseProvider databaseProvider;
    private RedisProvider redisProvider;

    private Gson gson;

    private GlmcApiProvider glmcApiProvider;

    @Override
    public void onLoad() {
        this.gson = new Gson();

        this.loadFiles();

        this.configProvider = new ConfigProvider(this);
        this.databaseProvider = new DatabaseProvider(this, this.configProvider.getDatabaseConfig());
        this.redisProvider = new RedisProvider(this, this.configProvider.getRedisConfig());
    }

    @Override
    public void onEnable() {
        this.glmcApiProvider = new GlmcApiProvider(this);
    }

    @Override
    public void onDisable() {
        this.redisProvider.unload();
        this.databaseProvider.unload();
    }

    private void loadFiles() {
        File configFile = new File(this.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            this.saveDefaultConfig();
        }
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

    public GlmcApiProvider getGlmcApiProvider() {
        return glmcApiProvider;
    }

    public Gson getGson() {
        return gson;
    }
}
