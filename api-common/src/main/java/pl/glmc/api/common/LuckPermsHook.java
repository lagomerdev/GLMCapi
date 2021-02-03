package pl.glmc.api.common;

import net.luckperms.api.LuckPerms;

import java.util.UUID;

/**
 * LuckPerms API Bridge
 */
public interface LuckPermsHook {

    /**
     * Gets user prefix from LuckPerms API.
     *
     * With refresh set to true, it MUST be called asynchronously
     * - in other case it may cause main server's thread lags
     * because it's blocking operation.
     *
     * @param playerUUID player's unique identifier
     * @param refresh specifies if cached data should be updated
     * @return user prefix
     */
    String getPrefix(UUID playerUUID, boolean refresh);

    /**
     * Gets user suffix from LuckPerms API.
     *
     * With refresh set to true, it MUST be called asynchronously
     * - in other case it may cause main server's thread lags
     * because it's blocking operation.
     *
     * @param playerUUID player's unique identifier
     * @param refresh specifies if cached data should be updated
     * @return user suffix
     */
    String getSuffix(UUID playerUUID, boolean refresh);

    /**
     * Gets user primary group from LuckPerms API.
     *
     * With refresh set to true, it MUST be called asynchronously
     * - in other case it may cause main server's thread lags
     * because it's blocking operation.
     *
     * @param playerUUID player's unique identifier
     * @param refresh specifies if cached data should be updated
     * @return user primary group
     */
    String getPrimaryGroup(UUID playerUUID, boolean refresh);

    /**
     * Gets value of given metadata key
     *
     * @param playerUUID player's unique identifier
     * @param metaKey key to get
     * @param refresh specifies if cached data should be updated
     * @return meta value of given key
     */
    String getMetaValue(UUID playerUUID, String metaKey, boolean refresh);

    /**
     * Checks if user has given permission using LuckPerms API.
     *
     * With refresh set to true, it MUST be called asynchronously
     * - in other case it may cause main server's thread lags
     * because it's blocking operation.
     *
     * @param playerUUID player's unique identifier
     * @param permission permission
     * @param refresh specifies if cached data should be updated
     * @return boolean which specifies if given player has given permission
     */
    boolean hasPermission(UUID playerUUID, String permission, boolean refresh);

    /**
     * Gets LuckPerms API instance
     *
     * @return LuckPerms API instance
     */
    LuckPerms getLuckPerms();
}
