package pl.glmc.core.bukkit.api.economy;

import pl.glmc.api.common.config.EconomyConfig;
import pl.glmc.api.common.economy.Economy;
import pl.glmc.api.common.economy.EconomyFactory;
import pl.glmc.core.bukkit.GlmcCoreBukkit;

import java.util.concurrent.ConcurrentHashMap;

public class ApiEconomyFactory implements EconomyFactory {
    private final GlmcCoreBukkit plugin;
    private final ConcurrentHashMap<String, Economy> registeredEconomies;

    public ApiEconomyFactory(GlmcCoreBukkit plugin) {
        this.plugin = plugin;
        this.registeredEconomies = new ConcurrentHashMap<>();
    }

    @Override
    public Economy registerEconomy(EconomyConfig economyConfig) {
        boolean registered = this.registeredEconomies.containsKey(economyConfig.getName());

        if (!registered) {
            ApiEconomyProvider apiEconomyProvider = new ApiEconomyProvider(this.plugin, economyConfig);
            apiEconomyProvider.register();

            this.registeredEconomies.put(economyConfig.getName(), apiEconomyProvider);

            return apiEconomyProvider;
        } else {
            throw new IllegalArgumentException("Given Economy name is already registered!");
        }
    }

    /*@Override
    public void unregisterEconomy(String economyName) {
        Economy economy = this.registeredEconomies.remove(economyName);

        if (economy != null) {
            //todo cleanup stuff
        }
    }*/

    @Override
    public ConcurrentHashMap<String, Economy> getRegisteredEconomies() {
        return this.registeredEconomies;
    }

    @Override
    public Economy getEconomy(String name) {
        Economy economy = this.registeredEconomies.getOrDefault(name, null);

        if (economy == null) {
            throw new NullPointerException("Economy with given is not registered!");
        }

        return economy;
    }
}
