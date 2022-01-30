package pl.glmc.core.common.packets;

import pl.glmc.api.common.packet.PacketInfo;
import pl.glmc.core.common.packets.bridge.PlaySound;
import pl.glmc.core.common.packets.server.ServerRegistrationRequest;
import pl.glmc.core.common.packets.server.ServerRegistrationResponse;

public class LocalPacketRegistry {

    public static final class SERVER {
        public static final PacketInfo REGISTRATION_REQUEST = PacketInfo.make("api.server.registration_request", ServerRegistrationRequest.class);
        public static final PacketInfo REGISTRATION_RESPONSE = PacketInfo.make("api.server.registration_response", ServerRegistrationResponse.class);
    }

    public static final class BRIDGE {
        public static final PacketInfo PLAY_SOUND = PacketInfo.make("api.server.play_sound", PlaySound.class);
    }
}
