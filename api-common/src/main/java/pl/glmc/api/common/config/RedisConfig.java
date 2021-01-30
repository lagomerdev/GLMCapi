package pl.glmc.api.common.config;

public class RedisConfig {

    private final String host, password;
    private final int port, timeout;

    /**
     * Reids connection credentials
     *
     * @param host host
     * @param password password
     * @param port port
     * @param timeout timeout
     */
    public RedisConfig(final String host, final String password, final int port, int timeout) {
        this.host = host;
        this.password = password;
        this.port = port;
        this.timeout = timeout;
    }

    /**
     * @return host
     */
    public String getHost() {
        return this.host;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * @return port
     */
    public int getPort() {
        return this.port;
    }

    /**
     * @return timeout
     */
    public int getTimeout() {
        return this.timeout;
    }
}
