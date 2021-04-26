package pl.glmc.core.bungee.api.user;

import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pl.glmc.api.bungee.event.UserJoinEvent;
import pl.glmc.api.bungee.user.CachedUserData;
import pl.glmc.api.bungee.user.User;
import pl.glmc.api.bungee.user.UserManager;
import pl.glmc.core.bungee.GlmcCoreBungee;
import pl.glmc.core.bungee.api.user.data.ApiCachedUserData;
import pl.glmc.core.bungee.api.user.data.ApiUser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ApiUserManager implements UserManager {
    private static final String JOIN_STATEMENT = "INSERT INTO `player_data` (`uuid`, `username`) " +
            "VALUES (?, ?) ON DUPLICATE KEY UPDATE `last_joined` = CURRENT_TIMESTAMP(), `username` = VALUES(`username`);";

    private final GlmcCoreBungee plugin;

    private final ConcurrentHashMap<UUID, User> userCache;
    private final ConcurrentHashMap<UUID, ApiCachedUserData> allRegisteredUsers;

    public ApiUserManager(GlmcCoreBungee plugin) {
        this.plugin = plugin;

        this.userCache = new ConcurrentHashMap<>();
        this.allRegisteredUsers = new ConcurrentHashMap<>();

        this.load();
    }

    private void load() {
        final String createTableStatement = "CREATE TABLE IF NOT EXISTS `player_data` ( " +
                " `uuid` char(36) NOT NULL, " +
                " `username` char(16) NOT NULL, " +
                " `last_joined` timestamp NOT NULL DEFAULT current_timestamp(), " +
                " `first_joined` timestamp NOT NULL DEFAULT current_timestamp(), " +
                " PRIMARY KEY (`uuid`) " +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

        this.plugin.getDatabaseProvider().updateSync(createTableStatement);

        this.loadAllUsers();

        ApiUserListener apiUserListener = new ApiUserListener(this.plugin, this);
    }

    private void loadAllUsers() {
        final String getAllUsersStatement = "SELECT * FROM `player_data`";

        ResultSet rs = this.plugin.getDatabaseProvider().getSync(getAllUsersStatement);

        try {
            while (rs.next()) {
                UUID playerUniqueId = UUID.fromString(rs.getString("uuid"));
                String playerUsername = rs.getString("username");
                Timestamp lastJoined = rs.getTimestamp("last_joined");
                Timestamp firstJoined = rs.getTimestamp("first_joined");

                ApiCachedUserData userData = new ApiCachedUserData(playerUsername, lastJoined, firstJoined);
                String userDataJson = this.plugin.getGson().toJson(userData);

                this.plugin.getRedisProvider().hset("api.users", playerUniqueId.toString(), userDataJson);

                this.allRegisteredUsers.put(playerUniqueId, userData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void test(UUID playerUniqueId, String username) {
        this.plugin.getDatabaseProvider().updateSync(JOIN_STATEMENT, playerUniqueId.toString(),username);

        ApiCachedUserData userData = this.allRegisteredUsers.compute(playerUniqueId, (uniqueId, cachedData) -> {
            Timestamp currentTimestamp = new Timestamp(new Date().getTime());
            if (cachedData != null) {
                cachedData.updateLastJoined(currentTimestamp);
                cachedData.updateUsername(username);

                return cachedData;
            } else {
                return new ApiCachedUserData(username, currentTimestamp, currentTimestamp);
            }
        });

        String userDataJson = this.plugin.getGson().toJson(userData.toString());
        this.plugin.getRedisProvider().hset("api.users", playerUniqueId.toString(), userDataJson);
    }

    //should be called only asynchronously to not impact main thread's performance!
    public void processLogin(PendingConnection connection) {
        this.plugin.getDatabaseProvider().updateSync(JOIN_STATEMENT, connection.getUniqueId().toString(), connection.getName());

        ApiCachedUserData userData = this.allRegisteredUsers.compute(connection.getUniqueId(), (uniqueId, cachedData) -> {
            Timestamp currentTimestamp = new Timestamp(new Date().getTime());
            if (cachedData != null) {
                cachedData.updateLastJoined(currentTimestamp);
                cachedData.updateUsername(connection.getName());

                return cachedData;
            } else {
                return new ApiCachedUserData(connection.getName(), currentTimestamp, currentTimestamp);
            }
        });

        String userDataJson = this.plugin.getGson().toJson(userData.toString());
        this.plugin.getRedisProvider().hset("api.users", connection.getUniqueId().toString(), userDataJson);
    }

    public void processJoin(ProxiedPlayer player) {
        User user = this.getUser(player.getUniqueId()).join();
        this.userCache.put(player.getUniqueId(), user);

        UserJoinEvent userJoinEvent = new UserJoinEvent(user);
        this.plugin.getProxy().getPluginManager().callEvent(userJoinEvent);
    }

    public void processDisconnect(UUID playerUniqueId) {
        this.userCache.remove(playerUniqueId);
    }

    @Override
    public CompletableFuture<User> loadUser(UUID playerUniqueId) {
        Objects.requireNonNull(playerUniqueId);

        CompletableFuture<User> user = new CompletableFuture<>();

        this.plugin.getProxy().getScheduler().runAsync(this.plugin, () -> {
           ApiCachedUserData userData = this.allRegisteredUsers.get(playerUniqueId);

           if (userData != null) {
               ProxiedPlayer player = this.plugin.getProxy().getPlayer(playerUniqueId);

               if (player == null) {
                   user.complete(null);

                   return;
               }

               ApiUser apiUser = new ApiUser(this.plugin, player, userData);
               user.complete(apiUser);
           } else {
               user.complete(null);
           }
        });

        return user;
    }

    @Override
    public CompletableFuture<User> loadUser(ProxiedPlayer player) {
        Objects.requireNonNull(player);

        return this.loadUser(player.getUniqueId());
    }

    @Override
    public User getCachedUser(UUID playerUniqueId) {
        Objects.requireNonNull(playerUniqueId);

        return this.userCache.get(playerUniqueId);
    }

    @Override
    public CompletableFuture<User> getUser(UUID playerUniqueId) {
        Objects.requireNonNull(playerUniqueId);

        if (this.userCache.containsKey(playerUniqueId)) {
            CompletableFuture<User> user = new CompletableFuture<>();

            User apiUser = this.userCache.get(playerUniqueId);

            user.complete(apiUser);

            return user;
        } else {
            return this.loadUser(playerUniqueId);
        }
    }

    @Override
    public boolean isLoaded(UUID playerUniqueId) {
        Objects.requireNonNull(playerUniqueId);

        return this.userCache.containsKey(playerUniqueId);
    }

    @Override
    public boolean exists(UUID playerUniqueId) {
        return this.allRegisteredUsers.containsKey(playerUniqueId);
    }

    @Override
    public String getUsername(UUID playerUniqueID) {
        CachedUserData userData = this.allRegisteredUsers.get(playerUniqueID);

        if (userData == null) {
            throw new NullPointerException("Cannot find username for given unique id!");
        } else {
            return userData.getUsername();
        }
    }

    @Override
    public String getUsername(UUID playerUniqueID, String defaultValue) {
        CachedUserData userData = this.allRegisteredUsers.get(playerUniqueID);

        return userData == null ? defaultValue : userData.getUsername();
    }

    @Override
    public Set<String> getRegisteredUsernames() {
        return this.allRegisteredUsers.values().stream().map(CachedUserData::getUsername)
                .collect(Collectors.toSet());
    }
}
