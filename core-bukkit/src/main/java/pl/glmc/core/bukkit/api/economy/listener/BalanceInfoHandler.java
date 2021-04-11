package pl.glmc.core.bukkit.api.economy.listener;

import pl.glmc.api.common.packet.listener.ResponseHandlerListener;
import pl.glmc.core.common.packets.LocalPacketRegistry;
import pl.glmc.core.common.packets.economy.BalanceResponse;

import java.math.BigDecimal;

public class BalanceInfoHandler extends ResponseHandlerListener<BalanceResponse, BigDecimal> {
    public BalanceInfoHandler() {
        super(LocalPacketRegistry.ECONOMY.BALANCE_RESPONSE, BalanceResponse.class);
    }

    @Override
    public void received(BalanceResponse packet) {
        this.complete(packet.getOriginUniqueId(), packet.getBalance());
    }
}
