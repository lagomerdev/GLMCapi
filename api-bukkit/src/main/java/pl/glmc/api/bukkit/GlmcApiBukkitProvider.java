package pl.glmc.api.bukkit;

public final class GlmcApiBukkitProvider {
    private static GlmcApiBukkit instance = null;

    public static GlmcApiBukkit get() {
        if (instance == null) {
            throw new NullPointerException("GlmcAPI is not loaded!");
        }        return instance;
    }

    public static void register(GlmcApiBukkit instance) {
        GlmcApiBukkitProvider.instance = instance;
    }

    public static void unregister() {
        GlmcApiBukkitProvider.instance = null;
    }
}
