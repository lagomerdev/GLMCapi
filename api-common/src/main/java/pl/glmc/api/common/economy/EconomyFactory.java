package pl.glmc.api.common.economy;

import pl.glmc.api.common.config.EconomyConfig;

import java.util.concurrent.ConcurrentHashMap;

public interface EconomyFactory {

    /**
     * Registers economy with given EconomyConfig
     *
     * @param economyConfig economy settings
     * @return registered economy instance
     */
    Economy registerEconomy(final EconomyConfig economyConfig);

    /*
      Unregisters economy with given economyName

      @param economyName economy name

    void unregisterEconomy(final String economyName);*/

    /**
     * Gets registered economies
     *
     * @return registered economies
     */
    ConcurrentHashMap<String, Economy> getRegisteredEconomies();

    /**
     * Gets economy with given name
     *
     * @param name economy name
     * @return registered economy instance or null if economy with given name is not registered
     */
    Economy getEconomy(String name);
}
