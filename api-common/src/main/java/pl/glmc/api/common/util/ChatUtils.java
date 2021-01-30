package pl.glmc.api.common.util;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides useful chat functionalities
 */
public class ChatUtils {
    private static final Pattern hexColor = Pattern.compile("&#([A-Fa-f0-9]){6}");

    /**
     * Applies colors to an uncolored message
     *
     * @param message uncolored message
     * @return colored message
     */
    public static String fixColor(String message) {
        Matcher matcher = hexColor.matcher(message);
        while (matcher.find()) {
            final ChatColor color = ChatColor.of(matcher.group().substring(1));
            final String origin = message.substring(0, matcher.start());
            final String modified = message.substring(matcher.end());

            message = origin + color + modified;
            matcher = hexColor.matcher(message);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
