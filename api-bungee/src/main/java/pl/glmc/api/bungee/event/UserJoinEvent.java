package pl.glmc.api.bungee.event;

import net.md_5.bungee.api.plugin.Event;
import pl.glmc.api.bungee.user.User;

public class UserJoinEvent extends Event {

    private final User user;

    public UserJoinEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
