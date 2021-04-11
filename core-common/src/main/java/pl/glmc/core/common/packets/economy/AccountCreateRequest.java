package pl.glmc.core.common.packets.economy;

import pl.glmc.api.common.packet.PacketInfo;
import pl.glmc.api.common.packet.RequestPacket;
import pl.glmc.core.common.packets.LocalPacketRegistry;

import java.util.UUID;

public class AccountCreateRequest extends RequestPacket {
    private static final PacketInfo PACKET_INFO = LocalPacketRegistry.ECONOMY.ACCOUNT_CREATE_REQUEST;

    private final UUID accountUniqueId;

    public AccountCreateRequest(UUID accountUniqueId) {
        this.accountUniqueId = accountUniqueId;
    }

    public UUID getAccountUniqueId() {
        return accountUniqueId;
    }

    @Override
    public String getPacketId() {
        return PACKET_INFO.getId();
    }
}
