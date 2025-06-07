package me.xiaoying.logger.utils;

public class ColorUtil {
    /**
     * 判断是否为 color 字符
     *
     * @param c 判断字符
     * @return 是否为 color 字符
     */
    public static boolean isColorKey(char c) {
        return "123456789AaBbCcDdEeFf".indexOf(c) != -1;
    }
}