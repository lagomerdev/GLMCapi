package pl.glmc.core.bungee;

import com.google.gson.Gson;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import pl.glmc.api.bungee.database.DatabaseProvider;
import pl.glmc.api.bungee.database.RedisProvider;
import pl.glmc.core.bungee.api.GlmcApiProvider;
import pl.glmc.core.bungee.api.user.data.ApiCachedUserData;
import pl.glmc.core.bungee.cmd.economy.BalanceCommand;
import pl.glmc.core.bungee.cmd.economy.BaltopCommand;
import pl.glmc.core.bungee.cmd.economy.EconomyCommand;
import pl.glmc.core.bungee.config.ConfigProvider;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

public class GlmcCoreBungee extends Plugin {
    private GlmcApiProvider apiProvider;

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
        this.apiProvider = new GlmcApiProvider(this);
        this.apiProvider.load();

        BalanceCommand balanceCommand = new BalanceCommand(this);
        BaltopCommand baltopCommand = new BaltopCommand(this);
        EconomyCommand economyCommand = new EconomyCommand(this);
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

    public GlmcApiProvider getApiProvider() {
        return apiProvider;
    }

    public String getServerId() {
        return "proxy";
    }
}
