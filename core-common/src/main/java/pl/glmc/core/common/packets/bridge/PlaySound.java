package pl.glmc.core.common.packets.bridge;

import pl.glmc.api.common.packet.InfoPacket;
import pl.glmc.api.common.packet.PacketInfo;
import pl.glmc.core.common.packets.LocalPacketRegistry;

import java.util.UUID;

public class PlaySound extends InfoPacket {
    private final static PacketInfo PACKET_INFO = LocalPacketRegistry.BRIDGE.PLAY_SOUND;

    private final UUID playerUniqueId;
    private final String sound;
    private final float pitch, volume;

    public PlaySound(UUID playerUniqueId, String sound, float pitch, float volume) {
        this.playerUniqueId = playerUniqueId;
        this.sound = sound;
        this.pitch = pitch;
        this.volume = volume;
    }

    public UUID getPlayerUniqueId() {
        return playerUniqueId;
    }

    public String getSound() {
        return sound;
    }

    public float getPitch() {
        return pitch;
    }

    public float getVolume() {
        return volume;
    }

    @Override
    public String getPacketId() {
        return PACKET_INFO.getId();
    }
}
