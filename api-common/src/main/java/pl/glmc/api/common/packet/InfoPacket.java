package pl.glmc.api.common.packet;

public abstract class InfoPacket implements Packet {
    private String sender;

    public InfoPacket() {
        //
    }

    @Override
    public void setSender(String sender) {
        this.sender = sender;
    }

    @Override
    public String getSender() {
        return this.sender;
    }
}
