package pl.glmc.api.bungee;

public class GlmcApiBungeeProvider {
    private static GlmcApiBungee instance = null;

    public static GlmcApiBungee get() {
        if (instance == null) {
            throw new NullPointerException("GlmcAPI is not loaded!");
        }        return instance;
    }

    public static void register(GlmcApiBungee instance) {
        GlmcApiBungeeProvider.instance = instance;
    }

    public static void unregister() {
        GlmcApiBungeeProvider.instance = null;
    }
}
