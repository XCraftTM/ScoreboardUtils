package de.xcrafttm.scoreboardUtils.tools;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Utils {

    private static final Map<Character, String> COLOR_MAP = new HashMap<>();

    static {
        // Colors
        COLOR_MAP.put('0', "<black>");
        COLOR_MAP.put('1', "<dark_blue>");
        COLOR_MAP.put('2', "<dark_green>");
        COLOR_MAP.put('3', "<dark_aqua>");
        COLOR_MAP.put('4', "<dark_red>");
        COLOR_MAP.put('5', "<dark_purple>");
        COLOR_MAP.put('6', "<gold>");
        COLOR_MAP.put('7', "<gray>");
        COLOR_MAP.put('8', "<dark_gray>");
        COLOR_MAP.put('9', "<blue>");
        COLOR_MAP.put('a', "<green>");
        COLOR_MAP.put('b', "<aqua>");
        COLOR_MAP.put('c', "<red>");
        COLOR_MAP.put('d', "<light_purple>");
        COLOR_MAP.put('e', "<yellow>");
        COLOR_MAP.put('f', "<white>");

        // Formats
        COLOR_MAP.put('l', "<bold>");
        COLOR_MAP.put('m', "<strikethrough>");
        COLOR_MAP.put('n', "<underlined>");
        COLOR_MAP.put('o', "<italic>");
        COLOR_MAP.put('r', "<reset>");
        COLOR_MAP.put('k', "<obfuscated>");
    }

    public static Component mm(String input) {
        return MiniMessage.miniMessage().deserialize(cnvMM(PlaceholderAPI.setPlaceholders(null, input)));
    }

    public static Component mm(String input, Player player) {
        return MiniMessage.miniMessage().deserialize(cnvMM(PlaceholderAPI.setPlaceholders(player, input)));
    }

    public static String cnvMM(String input) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if ((c == '&' || c == '§') && i + 1 < input.length()) {
                char code = Character.toLowerCase(input.charAt(i + 1));
                String tag = COLOR_MAP.get(code);
                if (tag != null) {
                    result.append(tag);
                    i++; // Skip next char (the color code)
                    continue;
                }
            }
            result.append(c);
        }

        return result.toString();
    }

    public static String stripColor(String input) {
        return input.replaceAll("(?i)([&§])[0-9A-FK-OR]", "");
    }
}
