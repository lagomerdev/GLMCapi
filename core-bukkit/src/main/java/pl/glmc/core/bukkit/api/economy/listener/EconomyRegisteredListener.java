package pl.glmc.core.bukkit.api.economy.listener;

import pl.glmc.api.common.packet.listener.PacketListener;
import pl.glmc.api.common.packet.listener.ResponseHandlerListener;
import pl.glmc.core.bukkit.GlmcCoreBukkit;
import pl.glmc.core.common.packets.LocalPacketRegistry;
import pl.glmc.core.common.packets.economy.EconomyRegistered;

public class EconomyRegisteredListener extends PacketListener<EconomyRegistered> {
    private final GlmcCoreBukkit plugin;

    public EconomyRegisteredListener(GlmcCoreBukkit plugin) {
        super(LocalPacketRegistry.ECONOMY.REGISTERED, EconomyRegistered.class);

        this.plugin = plugin;

        this.plugin.getApiProvider().getPacketService().registerListener(this, this.plugin);
    }

    @Override
    public void received(EconomyRegistered packet) {
        this.plugin.getApiProvider().getEconomyFactory().loadEconomy(packet.getEconomyConfig());
    }
}
