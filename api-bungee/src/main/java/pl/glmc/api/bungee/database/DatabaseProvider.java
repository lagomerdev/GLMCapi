package pl.glmc.api.bungee.database;

import com.zaxxer.hikari.HikariDataSource;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import pl.glmc.api.common.config.DatabaseConfig;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database API
 */
public class DatabaseProvider {
    private final Plugin plugin;

    private DatabaseConfig databaseConfig;

    private RowSetFactory rowSetFactory;
    private HikariDataSource dataSource;

    /**
     * Creates DatabaseProvider instance
     *
     * @param plugin instance of a plugin
     * @param databaseConfig config with mysql credentials and some pool settings
     */
    public DatabaseProvider(final Plugin plugin, DatabaseConfig databaseConfig) {
        this.plugin = plugin;
        this.databaseConfig = databaseConfig;

        this.load();
    }

    /**
     * Loads pl.glmc.core.bukkit.pl.glmc.glmc.api.bungee.database connection pool
     */
    public void load() {
        this.dataSource = new HikariDataSource();

        this.dataSource.setJdbcUrl("jdbc:mysql://" + this.databaseConfig.getHost() + ":" + this.databaseConfig.getPort() + "/"
                + this.databaseConfig.getDatabase() + "?autoReconnect=true");
        this.dataSource.setUsername(this.databaseConfig.getUsername());
        this.dataSource.setPassword(this.databaseConfig.getPassword());
        this.dataSource.setMaximumPoolSize(this.databaseConfig.getMaxPoolSize());
        this.dataSource.setConnectionTimeout(60000);
        this.dataSource.addDataSourceProperty("dataSource.cachePrepStmts", "true");
        this.dataSource.addDataSourceProperty("dataSource.prepStmtCacheSize", 250);
        this.dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        try {
            this.rowSetFactory = RowSetProvider.newFactory();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        try (final Connection testConnection = this.dataSource.getConnection()) {
            this.plugin.getLogger().info(ChatColor.GREEN + "Successfully connected to MYSQL!");
        } catch (SQLException exception) {
            exception.printStackTrace();

            this.plugin.getLogger().warning(ChatColor.RED + "An error occurred while trying to connect to MYSQL!");
            this.plugin.getLogger().warning(ChatColor.RED + "Make sure that mysql credentials are set up correctly in config.yml!");
        }
    }

    /**
     * Closes database connection pool
     */
    public void unload() {
        this.dataSource.close();
    }

    /**
     * Executes update synchronously
     *
     * @param statement sql statement to execute
     * @param params parameters to apply
     * @return true if success false if failed
     */
    public boolean updateSync(final String statement, final Object... params) {
        try (final Connection connection = this.dataSource.getConnection()) {
            final PreparedStatement update = connection.prepareStatement(statement);
            this.applyParams(update, params);

            update.executeUpdate();
            update.close();

            return true;
        } catch (SQLException exception) {
            exception.printStackTrace();

            return false;
        }
    }

    /**
     * Executes update asynchronously
     *
     * @param statement sql statement to execute
     * @param params parameters to apply
     */
    public void updateAsync(final String statement, final Object... params) {
        this.plugin.getProxy().getScheduler().runAsync(this.plugin, () -> {
            try (final Connection connection = this.dataSource.getConnection()) {
                final PreparedStatement update = connection.prepareStatement(statement);
                this.applyParams(update, params);

                update.executeUpdate();
                update.close();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Executes query synchronously
     *
     * @param statement sql statement to execute
     * @param params parameters to apply
     * @return query response
     */
    public CachedRowSet getSync(final String statement, final Object... params) {
        try (final Connection connection = this.dataSource.getConnection(); final PreparedStatement query = connection.prepareStatement(statement)) {
            this.applyParams(query, params);

            CachedRowSet crs = rowSetFactory.createCachedRowSet();
            crs.populate(query.executeQuery());
            return crs;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    /**
     * Executes query asynchronously
     *
     * @param callback called after query completion with ResultSet or Exception
     * @param statement sql statement to execute
     * @param params parameters to apply
     */
    public void getAsync(final Callback<ResultSet, Throwable> callback, final String statement, final Object... params) {
        this.plugin.getProxy().getScheduler().runAsync(this.plugin, () -> {
            try (final Connection connection = this.dataSource.getConnection()) {
                final PreparedStatement query = connection.prepareStatement(statement);
                this.applyParams(query, params);

                callback.done(query.executeQuery(), null);

                query.close();
            } catch (SQLException exception) {
                exception.printStackTrace();

                callback.done(null, exception);
            }
        });
    }

    private void applyParams(final PreparedStatement statement, final Object... params) {
        try {
            int i = 1;
            for (Object param : params) {
                statement.setObject(i, param);

                i++;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     *
     * @return pl.glmc.core.bukkit.pl.glmc.glmc.api.bungee.database data source
     */
    public HikariDataSource getDataSource() {
        return this.dataSource;
    }

    public interface Callback<ResultSet, Throwable> {
        void done(ResultSet resultSet, Throwable throwable);
    }
}