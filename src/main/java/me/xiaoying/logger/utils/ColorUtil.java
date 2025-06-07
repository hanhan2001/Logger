package me.xiaoying.logger.utils;

import me.xiaoying.logger.ChatColor;

public class ColorUtil {
    public static String translate(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    /**
     * 判断是否为 color 字符
     *
     * @param c 判断字符
     * @return 是否为 color 字符
     */
    public static boolean isColorKey(char c) {
        return "123456789AaBbCcDdEeFfRr".indexOf(c) != -1;
    }
}