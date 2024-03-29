package pl.glmc.core.bukkit.api.hook;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import pl.glmc.api.common.LuckPermsHook;
import pl.glmc.core.bukkit.GlmcCoreBukkit;

import java.util.UUID;

public class ApiLuckPermsHook implements LuckPermsHook {
    private final GlmcCoreBukkit plugin;

    private LuckPerms luckPerms;

    public ApiLuckPermsHook(GlmcCoreBukkit plugin) {
        this.plugin = plugin;

        this.init();
    }

    private void init() {
        RegisteredServiceProvider<LuckPerms> provider = this.plugin.getServer().getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            this.luckPerms = provider.getProvider();

            this.plugin.getLogger().info(ChatColor.GREEN + "Successfully hooked into LuckPerms API!");
        } else {
            this.plugin.getLogger().warning(ChatColor.RED + "LuckPerms API provider not found!");
        }
    }

    private User getUser(UUID playerUUID, boolean refresh) {
        User user;

        if (refresh) {
            user = this.luckPerms.getUserManager().loadUser(playerUUID).join();
        } else {
            user = this.luckPerms.getUserManager().getUser(playerUUID);
        }

        return user;
    }

    @Override
    public String getPrefix(UUID playerUUID, boolean refresh) {
        User user = this.getUser(playerUUID, refresh);

        if (user == null) {
            throw new NullPointerException("Failed getting user from LuckPerms!");
        }

        String prefix = user.getCachedData().getMetaData().getPrefix();

        if (prefix == null) prefix = "";

        return prefix;
    }

    @Override
    public String getSuffix(UUID playerUUID, boolean refresh) {
        User user = this.getUser(playerUUID, refresh);

        if (user == null) {
            throw new NullPointerException("Failed getting user from LuckPerms!");
        }

        String suffix = user.getCachedData().getMetaData().getSuffix();

        if (suffix == null) suffix = "";

        return suffix;
    }

    @Override
    public String getPrimaryGroup(UUID playerUUID, boolean refresh) {
        User user = this.getUser(playerUUID, refresh);

        if (user == null) {
            throw new NullPointerException("Failed getting user from LuckPerms!");
        }

        return user.getPrimaryGroup();
    }

    @Override
    public String getMetaValue(UUID playerUUID, String metaKey, boolean refresh) {
        User user = this.getUser(playerUUID, refresh);

        if (user == null) {
            throw new NullPointerException("Failed getting user from LuckPerms!");
        }

        return user.getCachedData().getMetaData().getMetaValue(metaKey);
    }

    @Override
    public boolean hasPermission(UUID playerUUID, String permission, boolean refresh) {
        User user = this.getUser(playerUUID, refresh);

        if (user == null) {
            throw new NullPointerException("Failed getting user from LuckPerms!");
        }

        return user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
    }

    @Override
    public LuckPerms getLuckPerms() {
        return this.luckPerms;
    }
}
