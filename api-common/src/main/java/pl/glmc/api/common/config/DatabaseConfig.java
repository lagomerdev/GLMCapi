package pl.glmc.api.common.config;

public class DatabaseConfig {

    private final String host, database, username, password, poolName;
    private final int port, maxPoolSize;

    /**
     * Database connection credentials
     *  @param host host
     * @param database database
     * @param username username
     * @param password password
     * @param poolName
     * @param port port
     * @param maxPoolSize max pool size
     */
    public DatabaseConfig(final String host, final String database, final String username, final String password, String poolName, final int port, final int maxPoolSize) {
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
        this.poolName = poolName;
        this.port = port;
        this.maxPoolSize = maxPoolSize;
    }

    /**
     * @return host
     */
    public String getHost() {
        return this.host;
    }

    /**
     * @return database
     */
    public String getDatabase() {
        return this.database;
    }

    /**
     * @return username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     *
     * @return
     */
    public String getPoolName() {
        return poolName;
    }

    /**
     * @return port
     */
    public int getPort() {
        return this.port;
    }

    /**
     * @return max pool size
     */
    public int getMaxPoolSize() {
        return this.maxPoolSize;
    }
}
