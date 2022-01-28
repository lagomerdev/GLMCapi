package pl.glmc.api.bungee.user;

import java.sql.Timestamp;

public interface CachedUserData {

    /**
     *
      * @return
     */
   String getUsername();

    /**
     *
     * @return
     */
    public Timestamp getLastJoined();

    /**
     *
     * @return
     */
    public Timestamp getFirstJoined();
}
