
package dev.gether.getsprawdzanie.utils;

import org.bukkit.ChatColor;

import java.util.List;

public class ColorFixer {

    public static String addColors(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        input = ChatColor.translateAlternateColorCodes('&', input);
        return input;
    }


    public static List<String> addColors(List<String> input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        for (int i = 0; i < input.size(); i++) {
            input.set(i, addColors(input.get(i)));
        }
        return input;
    }

}
