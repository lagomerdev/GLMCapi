package pl.glmc.api.common.economy;

import pl.glmc.api.common.config.EconomyConfig;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface EconomyFactory {

    /**
     *
     * @param economyListener
     */
    void registerListener(final EconomyListener economyListener);

    /**
     *
     * @param economyConfig
     */
    void registerEconomy(final EconomyConfig economyConfig);

    /**
     *
     * @param economyConfig
     * @return
     */
    Economy loadEconomy(final EconomyConfig economyConfig);

    /**
     * Gets registered economies
     *
     * @return registered economies
     */
    ConcurrentHashMap<String, Economy> getRegisteredEconomies();

    /**
     *
     * @return
     */
    HashSet<EconomyConfig> getRegisteredConfigs();

    /**
     * Gets economy with given name
     *
     * @param name economy name
     * @return registered economy instance or null if economy with given name is not registered
     */
    Economy getEconomy(String name);
}
