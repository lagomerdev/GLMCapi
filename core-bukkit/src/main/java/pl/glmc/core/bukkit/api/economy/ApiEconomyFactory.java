package pl.glmc.core.bukkit.api.economy;

import net.md_5.bungee.api.ChatColor;
import pl.glmc.api.common.config.EconomyConfig;
import pl.glmc.api.common.economy.Economy;
import pl.glmc.api.common.economy.EconomyFactory;
import pl.glmc.api.common.economy.EconomyListener;
import pl.glmc.core.bukkit.GlmcCoreBukkit;
import pl.glmc.core.bukkit.api.economy.listener.EconomyRegisteredListener;
import pl.glmc.core.bukkit.api.economy.listener.EconomyRegistrationHandler;
import pl.glmc.core.common.packets.economy.EconomyRegistrationRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ApiEconomyFactory implements EconomyFactory {
    private final GlmcCoreBukkit plugin;
    private final EconomyRegistrationHandler registrationHandler;
    private final ConcurrentHashMap<String, Economy> registeredEconomies;
    private final HashSet<EconomyConfig> registeredConfigs;
    private final HashSet<EconomyListener> registeredListeners;

    public ApiEconomyFactory(final GlmcCoreBukkit plugin) {
        this.plugin = plugin;

        this.registeredEconomies = new ConcurrentHashMap<>();
        this.registeredConfigs = new HashSet<>();
        this.registeredListeners = new HashSet<>();
        this.registrationHandler = new EconomyRegistrationHandler(this.plugin);

        EconomyRegisteredListener registeredListener = new EconomyRegisteredListener(this.plugin);
    }

    @Override
    public Economy loadEconomy(EconomyConfig economyConfig) {
        boolean loaded = this.registeredEconomies.containsKey(economyConfig.getName());

        if (!loaded) {
            ApiEconomyProvider apiEconomyProvider = new ApiEconomyProvider(this.plugin, economyConfig);
            apiEconomyProvider.load();

            this.registeredEconomies.put(economyConfig.getName(), apiEconomyProvider);
            this.registeredConfigs.add(economyConfig);

            for (EconomyListener registeredListener : this.registeredListeners) {
                registeredListener.loaded(apiEconomyProvider);
            }

            this.plugin.getLogger().info(ChatColor.GREEN + "Loaded economy " + ChatColor.DARK_GREEN + economyConfig.getName());

            return apiEconomyProvider;
        } else {
            throw new IllegalArgumentException("Given Economy name is already registered!");
        }
    }

    @Override
    public void registerListener(final EconomyListener economyListener) {
        if (this.registeredListeners.contains(economyListener)) {
            throw new IllegalArgumentException("This listener has been already registered!");
        }

        this.registeredListeners.add(economyListener);

        this.plugin.getLogger().info(ChatColor.GREEN + "Registered economy listener with " + economyListener.getClass().getName());
    }

    @Override
    public void registerEconomy(EconomyConfig economyConfig) {
        boolean registered = this.registeredEconomies.containsKey(economyConfig.getName());

        if (!registered) {
            EconomyRegistrationRequest packet = new EconomyRegistrationRequest(economyConfig);
            this.registrationHandler.create(packet.getUniqueId())
                    .completeOnTimeout(false, 1, TimeUnit.SECONDS)
                    .thenAccept(success -> {
                        if (success) {
                            this.plugin.getLogger().info(ChatColor.GREEN + "Registration of economy " + economyConfig.getName() + " has been accepted!");

                            this.loadEconomy(economyConfig);
                        } else {
                            this.plugin.getLogger().info(ChatColor.RED + "Registration of economy " + economyConfig.getName() + " has been denied!");
                        }
                    });

            this.plugin.getApiProvider().getPacketService().sendPacket(packet, "proxy");
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
    public HashSet<EconomyConfig> getRegisteredConfigs() {
        return this.registeredConfigs;
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
