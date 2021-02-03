package pl.glmc.api.common.config;

import pl.glmc.api.common.EconomyType;

public class EconomyConfig {

    private String name;
    private EconomyType economyType;

    public EconomyConfig(String name, EconomyType economyType) {
        this.name = name;
        this.economyType = economyType;
    }

    public String getName() {
        return name;
    }

    public EconomyType getEconomyType() {
        return economyType;
    }
}
