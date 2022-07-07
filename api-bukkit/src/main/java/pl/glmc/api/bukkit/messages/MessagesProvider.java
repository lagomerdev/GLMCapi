package pl.glmc.api.bukkit.messages;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;

public class MessagesProvider {

    private final Plugin plugin;
    private final HashMap<String, String> messages = new HashMap<>();

    private File messagesFile;
    private YamlConfiguration messagesData;

    public MessagesProvider(Plugin plugin, boolean debug) {
        this.plugin = plugin;

        this.load(debug);
    }

    private void load(boolean debug) {
        this.messagesFile = new File(this.plugin.getDataFolder(), "messages.yml");
        if (!messagesFile.exists() || debug) {
            this.plugin.saveResource("messages.yml", true);
        }

        this.messagesData = YamlConfiguration.loadConfiguration(messagesFile);

        this.loadMessages();
    }

    /**
     *
     */
    private void loadMessages() {
        for (String key : this.messagesData.getKeys(true)) {
            String message;
            if (this.messagesData.isString(key)) {
                message = this.messagesData.getString(key);
            } else if (this.messagesData.isList(key)) {
                StringBuilder messageBuilder = new StringBuilder();
                boolean first = true;

                for (String line : this.messagesData.getStringList(key)) {
                    if (first) first = false;
                    else messageBuilder.append("\n");

                    messageBuilder.append(line);
                }

                message = messageBuilder.toString();
            } else {
                //invalid message
                continue;
            }

            System.out.println(key + " : " + message);
            messages.put(key, message);
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

        return LegacyComponentSerializer.legacyAmpersand()
                .deserialize(message);
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

        return LegacyComponentSerializer.legacyAmpersand()
                .deserialize(String.format(message, replacements));

    }
}
