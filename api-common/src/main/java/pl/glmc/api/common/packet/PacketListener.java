package pl.glmc.api.common.packet;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;
import net.md_5.bungee.api.ChatColor;

import java.io.NotActiveException;
import java.lang.reflect.ParameterizedType;
import java.nio.charset.MalformedInputException;

public abstract class PacketListener<T extends Packet> {
    protected final PacketInfo packetInfo;
    private final Class<T> packetClass;

    private Gson gson;
    private boolean registered;

    /**
     *
     * @param packetInfo
     */
    public PacketListener(final PacketInfo packetInfo, final Class<T> packetClass) {
        this.packetInfo = packetInfo;
        this.packetClass = packetClass;

        this.registered = false;
    }

    /**
     *
     * @return
     */
    public PacketInfo getPacketInfo() {
        return this.packetInfo;
    }

    /**
     *
     * @param gson
     * @throws IllegalAccessException
     */
    private void register(final Gson gson) throws IllegalAccessException {
        if (registered) {
            throw new IllegalAccessException("This listener has been already registered!");
        }

        this.gson = gson;
        this.registered = true;
    }

    /**
     *
     * @param packetJson
     * @throws NotActiveException
     */
    public void process(final String packetJson) throws NotActiveException, MalformedJsonException {
        if (!registered) {
            throw new NotActiveException("This listener hasn't been registered yet!");
        }

        try {
            T packet = this.gson.fromJson(packetJson, packetClass);

            this.received(packet);
        } catch (JsonSyntaxException | ClassCastException e) {
            throw new MalformedJsonException("Received malformed packet json data!");
        }
    }

    /**
     *
     * @param packet
     */
    abstract public void received(final T packet);
}
