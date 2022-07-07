package pl.glmc.api.bungee.message;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;

public class MessagesProvider {

    private final Plugin plugin;
    private final HashMap<String, String> messages = new HashMap<>();

    private File messagesFile;
    private Configuration messagesConfig;

    public MessagesProvider(Plugin plugin, boolean debug) {
        this.plugin = plugin;

        this.load(debug);
    }

    private void load(boolean debug) {
        try {
            if (!this.plugin.getDataFolder().exists()) {
                this.plugin.getDataFolder().mkdir();
            }

            final File messagesFile = new File(this.plugin.getDataFolder(), "messages.yml");
            if (!messagesFile.exists() || debug) {
                if (messagesFile.exists()) messagesFile.delete();

                try (InputStream inputStream = this.plugin.getResourceAsStream("messages.yml")) {
                    Files.copy(inputStream, messagesFile.toPath());
                }
            }

            this.messagesConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(this.plugin.getDataFolder(), "messages.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.loadMessagesRecursively(messagesConfig, "");
    }

    /**
     *
     */
    private void loadMessagesRecursively(Configuration configuration, String path) {
        for (String key : configuration.getKeys()) {
            String message = configuration.getString(key);
            if (message.isBlank() && !configuration.getStringList(key).isEmpty()) {
                StringBuilder messageBuilder = new StringBuilder();
                boolean first = true;

                for (String line : configuration.getStringList(key)) {
                    if (first) first = false;
                    else messageBuilder.append("\n");

                    messageBuilder.append(line);
                }

                message = messageBuilder.toString();
            }

            if (message.isBlank()) {
                var section = configuration.getSection(key);
                if (section != null && !section.getKeys().isEmpty()) this.loadMessagesRecursively(configuration.getSection(key), path + key + ".");
            } else {
                System.out.println(path + key + " : " + message);
                messages.put(path + key, ChatColor.translateAlternateColorCodes('&', message));
            }
        }
    }

    /**
     *
     * @param key
     * @return
     */
    public TextComponent getMessage(String key) {
        var message = this.messages.get(key);
        if (message == null) return null;

        return new TextComponent(message);
    }

    /**
     *
     * @param key
     * @param replacements
     * @return
     */
    public TextComponent getMessage(String key, Object... replacements) {
        var message = this.messages.get(key);
        if (message == null) return null;

        return new TextComponent(String.format(message, replacements));
    }
}
