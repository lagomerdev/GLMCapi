package pl.glmc.api.bungee.user;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public interface UserManager {

    /**
     *
     * @param playerUniqueId
     * @return
     */
    CompletableFuture<User> loadUser(UUID playerUniqueId);

    /**
     *
     * @param player
     * @return
     */
    CompletableFuture<User> loadUser(ProxiedPlayer player);

    /**
     *
     * @param playerUniqueId
     * @return
     */
    User getCachedUser(UUID playerUniqueId);

    /**
     *
     * @param playerUniqueId
     * @return
     */
    CompletableFuture<User> getUser(UUID playerUniqueId);

    /**
     *
     * @param playerUniqueId
     * @return
     */
    boolean isLoaded(UUID playerUniqueId);

    /**
     *
     * @param playerUniqueId
     * @return
     */
    boolean exists(UUID playerUniqueId);

    /**
     * @throws NullPointerException if username for given unique id is not cached
     *
     * @param playerUniqueID
     * @return
     *
     */
    String getUsername(UUID playerUniqueID);

    /**
     *
     * @param playerUniqueID
     * @param defaultValue
     * @return
     */
    String getUsername(UUID playerUniqueID, String defaultValue);

    /**
     *
     * @param username
     * @return
     */
    UUID getUniqueId(String username);

    /**
     * Unmodifiable set of registered usernames
     *
     * @return
     */
    Set<String> getRegisteredUsernames();
}
