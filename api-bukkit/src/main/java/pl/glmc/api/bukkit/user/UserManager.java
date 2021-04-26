package pl.glmc.api.bukkit.user;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface UserManager {

    /**
     *
     * @param playerUniqueId
     * @return
     */
    CompletableFuture<User> loadUser(UUID playerUniqueId);

    /**
     *
     * @param playerUniqueId
     * @return
     */
    User getUser(UUID playerUniqueId);

    /**
     *
     * @param playerUniqueId
     * @return
     */
    boolean isLoaded(UUID playerUniqueId);
}
