package pl.glmc.core.bungee.api.user.data;

import pl.glmc.api.bungee.user.CachedUserData;

import java.sql.Timestamp;

public class ApiCachedUserData implements CachedUserData {

    private final Timestamp firstJoined;

    private String username;
    private Timestamp lastJoined;

    public ApiCachedUserData(String username, Timestamp lastJoined, Timestamp firstJoined) {
        this.username = username;
        this.lastJoined = lastJoined;
        this.firstJoined = firstJoined;
    }

    public void updateLastJoined(Timestamp lastJoined) {
        this.lastJoined = lastJoined;
    }

    public void updateUsername(String username) {
        this.username = username;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Timestamp getLastJoined() {
        return lastJoined;
    }

    @Override
    public Timestamp getFirstJoined() {
        return firstJoined;
    }
}