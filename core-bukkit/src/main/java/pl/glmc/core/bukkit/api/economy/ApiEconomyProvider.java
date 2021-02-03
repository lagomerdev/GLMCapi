package pl.glmc.core.bukkit.api.economy;

import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.NotImplementedException;
import pl.glmc.api.common.EconomyType;
import pl.glmc.api.common.config.EconomyConfig;
import pl.glmc.api.common.economy.Economy;
import pl.glmc.core.bukkit.GlmcCoreBukkit;
import pl.glmc.core.bukkit.api.economy.tasks.AddBalanceTask;
import pl.glmc.core.bukkit.api.economy.tasks.RefreshBalanceTask;
import pl.glmc.core.bukkit.api.economy.tasks.RemoveBalanceTask;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

public class ApiEconomyProvider implements Economy {
    private String insertLogStatement, createAccountStatement , getDataStatement, redisNotifyChannel;

    private final GlmcCoreBukkit plugin;
    private final EconomyConfig economyConfig;
    private final ConcurrentHashMap<UUID, BigDecimal> accountsCache;
    private final List<UUID> ignoreCacheRefresh;
    private final ExecutorService economyTasksExecutor;

    public ApiEconomyProvider(GlmcCoreBukkit plugin, EconomyConfig economyConfig) {
        this.plugin = plugin;
        this.economyConfig = economyConfig;

        this.accountsCache = new ConcurrentHashMap<>();
        this.ignoreCacheRefresh = new ArrayList<>();
        this.economyTasksExecutor = Executors.newSingleThreadExecutor();

        this.plugin.getLogger().info(ChatColor.GREEN + "Created Economy " + economyConfig.getName());
    }

    public void register() {
        final String economyDataTableName = "economy_" + economyConfig.getName() + "_data";
        final String economyLogsTableName = "economy_" + economyConfig.getName() + "_logs";

        this.insertLogStatement =  "INSERT INTO `" + economyLogsTableName  + "` (`account_uuid`, `amount`, `action`) VALUES (?, ?, ?)";
        this.getDataStatement = "SELECT * FROM `" + economyDataTableName + "` WHERE `uuid` = ?";
        this.createAccountStatement = "INSERT INTO `" + economyDataTableName + "` (`uuid`, `balance`, `active`) VALUES (?, ?, ?)";
        this.redisNotifyChannel = "economy." + economyConfig.getName() + ".notify";

        final String economyDataTable = "CREATE TABLE IF NOT EXISTS `" + economyDataTableName + "` ( " +
                " `uuid` char(36) NOT NULL, " +
                " `balance` decimal(12,2) NOT NULL, " +
                " `active` tinyint(1) NOT NULL, " +
                " UNIQUE KEY `uuid` (`uuid`) " +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

        this.plugin.getDatabaseProvider().updateSync(economyDataTable);

        final String economyLogsTable = "CREATE TABLE IF NOT EXISTS `" + economyLogsTableName + "` ( " +
                " `id` int(10) unsigned NOT NULL AUTO_INCREMENT, " +
                " `account_uuid` char(36) NOT NULL, " +
                " `amount` decimal(12,2) unsigned NOT NULL, " +
                " `action` tinyint(3) unsigned NOT NULL, " +
                " PRIMARY KEY (`id`) " +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

        this.plugin.getDatabaseProvider().updateSync(economyLogsTable);

        final String economyDataUpdateTrigger = "CREATE DEFINER= " + this.plugin.getConfigProvider().getDatabaseConfig().getUsername() +  "@" + this.plugin.getConfigProvider().getDatabaseConfig().getHost() + " " +
                "TRIGGER IF NOT EXISTS update_economy_" + economyConfig.getName() + "_data_update " +
                "BEFORE INSERT ON " + economyLogsTableName + " " +
                "FOR EACH ROW " +
                "BEGIN " +
                "IF (new.action = 1) THEN " +
                "UPDATE `" + economyDataTableName + "` SET `balance` = balance + new.amount WHERE uuid = new.account_uuid; " +
                "ELSE " +
                "UPDATE `" + economyDataTableName + "` SET `balance` = balance - new.amount WHERE uuid = new.account_uuid; " +
                "END IF; " +
                "END";

        this.plugin.getDatabaseProvider().updateSync(economyDataUpdateTrigger);

        ApiEconomyListener apiEconomyListener = new ApiEconomyListener(this);

        this.plugin.getRedisProvider().subscribe(apiEconomyListener, this.redisNotifyChannel);

        this.plugin.getLogger().info(ChatColor.DARK_GREEN + "Registered economy " + this.economyConfig.getName());
    }

    public boolean insertLog(UUID accountUUID, BigDecimal amount, boolean action) {
        boolean success = this.plugin.getDatabaseProvider().updateSync(insertLogStatement, accountUUID.toString(), amount, action);

        if (success) {
            final BigDecimal current = this.accountsCache.get(accountUUID);
            final BigDecimal update = action ? current.add(amount) : current.subtract(amount);

            this.accountsCache.put(accountUUID, update);
            this.ignoreCacheRefresh.add(accountUUID);

            this.plugin.getRedisProvider().publish(this.redisNotifyChannel, accountUUID.toString());
        }

        return success;
    }

    public void getAndUpdate(CompletableFuture<Void> blockingFuture, UUID accountUUID) {
        this.plugin.getDatabaseProvider().getAsync((resultSet, throwable) -> {
            try {
                if (resultSet.next()) {
                    final BigDecimal balance = resultSet.getBigDecimal("balance");
                    this.accountsCache.put(accountUUID, balance);

                    blockingFuture.complete(null);
                } else {
                    blockingFuture.cancel(true);

                    throw new NullPointerException("Account with given unique identifier does not exist!");
                }
            } catch (SQLException exception) {
                blockingFuture.complete(null);

                exception.printStackTrace();
            }
        }, this.getDataStatement, accountUUID.toString());
    }

    public void refreshCachedData(UUID accountUUID) {
        RefreshBalanceTask refreshBalanceTask = new RefreshBalanceTask(this, accountUUID);

        this.economyTasksExecutor.submit(refreshBalanceTask);
    }

    protected boolean checkRefreshIgnored(UUID accountUUID) {
        boolean contains = this.ignoreCacheRefresh.contains(accountUUID);
        if (contains) this.ignoreCacheRefresh.remove(accountUUID);

        return contains;
    }

    @Override
    public void cacheAccount(UUID accountUUID) {
        this.plugin.getDatabaseProvider().getAsync((resultSet, throwable) -> {
            try {
                final BigDecimal balance;
                if (resultSet.next()) {
                    balance = resultSet.getBigDecimal("balance");
                } else {
                    balance = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

                    this.plugin.getDatabaseProvider().updateAsync(createAccountStatement, accountUUID.toString(), balance, true);
                }
                this.accountsCache.put(accountUUID, balance);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }, this.getDataStatement, accountUUID.toString());
    }

    @Override
    public void removeFromCache(UUID accountUUID) {
        this.accountsCache.remove(accountUUID);
    }

    @Override
    public boolean isCached(UUID accountUUID) {
        return this.accountsCache.containsKey(accountUUID);
    }

    @Override
    public CompletableFuture<BigDecimal> getBalance(UUID accountUUID) {
        final CompletableFuture<BigDecimal> result = new CompletableFuture<>();

        final BigDecimal balance = this.accountsCache.getOrDefault(accountUUID, null);
        if (balance == null) {
            this.plugin.getDatabaseProvider().getAsync((resultSet, throwable) -> {
                try {
                    if (resultSet.next()) {
                        final BigDecimal balanceResult = resultSet.getBigDecimal("balance");

                        result.complete(balanceResult);
                    } else {
                        result.cancel(true);

                        throw new NullPointerException("Account with given unique identifier does not exist!");
                    }
                } catch (SQLException exception) {
                    result.cancel(true);

                    exception.printStackTrace();
                }
            }, getDataStatement, accountUUID.toString());
        } else {
            result.complete(balance);
        }

        return result;
    }

    @Override
    public CompletableFuture<Boolean> add(UUID accountUUID, BigDecimal amount) {
        if (amount.signum() != 1) {
            throw new IllegalArgumentException("Specified amount must be greater than zero!");
        }

        final CompletableFuture<Boolean> result = new CompletableFuture<>();
        final AddBalanceTask addBalanceTask = new AddBalanceTask(result, this, accountUUID, amount);

        this.economyTasksExecutor.submit(addBalanceTask);

        return result;
    }

    @Override
    public CompletableFuture<Boolean> remove(UUID accountUUID, BigDecimal amount) {
        if (amount.signum() != 1) {
            throw new IllegalArgumentException("Specified amount must be greater than zero!");
        }

        final CompletableFuture<Boolean> result = new CompletableFuture<>();
        final RemoveBalanceTask removeBalanceTask = new RemoveBalanceTask(result, this, accountUUID, amount);

        this.economyTasksExecutor.submit(removeBalanceTask);

        return result;
    }

    @Override
    public CompletableFuture<Boolean> transfer(UUID accountUUID, BigDecimal amount, Economy economy) {
        return this.transfer(accountUUID, accountUUID, amount, economy);
    }

    @Override
    public CompletableFuture<Boolean> transfer(UUID accountUUID, UUID targetUUID, BigDecimal amount, Economy economy) {
        if (amount.signum() != 1) {
            throw new IllegalArgumentException("Specified amount must be greater than zero!");
        }

        final CompletableFuture<Boolean> response = new CompletableFuture<>();

        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> {
            final CompletableFuture<Boolean> removed = this.remove(accountUUID, amount);
            if (!removed.join()) {
                response.complete(false);

                return;
            }

            final CompletableFuture<Boolean> added = economy.add(targetUUID, amount);
            if (added.join()) {
                response.complete(true);
            } else {
                final CompletableFuture<Boolean> returned = this.add(accountUUID, amount);

                if (!returned.join()) {
                    throw new RuntimeException("Failed to transfer amount back after failed adding in other economy!");
                } else {
                    response.complete(true);
                }
            }
        });

        return response;
    }

    @Override
    public void reset(UUID accountUUID) {
        throw new NotImplementedException("This future hasn't been implemented yet!");
    }

    @Override
    public BigDecimal getCachedBalance(UUID accountUUID) {
        final BigDecimal balance = this.accountsCache.getOrDefault(accountUUID, null);
        if (balance == null) {
            throw new IllegalArgumentException("This account does not exist/is unloaded!");
        }

        return balance;
    }

    @Override
    public EconomyConfig getEconomyConfig() {
        return this.economyConfig;
    }

    @Override
    public EconomyType getEconomyType() {
        return this.economyConfig.getEconomyType();
    }
}
