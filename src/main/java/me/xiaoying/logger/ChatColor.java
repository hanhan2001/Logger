package me.xiaoying.logger;

public enum ChatColor {
    BLACK('0', 0),
    DARK_BLUE('1', 1),
    DARK_GREEN('2', 2),
    DARK_AQUA('3', 3),
    DARK_RED('4', 4),
    DARK_PURPLE('5', 5),
    GOLD('6', 6),
    GRAY('7', 7),
    DARK_GRAY('8', 8),
    BLUE('9', 9),
    GREEN('a', 10),
    AQUA('b', 11),
    RED('c', 12),
    LIGHT_PURPLE('d', 13),
    YELLOW('e', 14),
    WHITE('f', 15);

    private final char code;
    private final int intCode;

    ChatColor(char code, int intCode) {
        this.code = code;
        this.intCode = intCode;
    }

    public char getCode() {
        return this.code;
    }

    public int getIntCode() {
        return this.intCode;
    }

    /**
     * 将关键字替换成颜色代码
     *
     * @param altCharColor 希望被替换关键字
     * @param text 原内容
     * @return 替换后的内容
     */
    public static String translateAlternateColorCodes(char altCharColor, String text) {
        if (!text.contains(String.valueOf(altCharColor)))
            return text;

        char[] chars = text.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != altCharColor && "123456789AaBbCcDdEeFf".indexOf(chars[i + 1]) == -1)
                continue;

            chars[i] = 167;
            chars[i + 1] = Character.toLowerCase(chars[i + 1]);
        }

        return new String(chars);
    }

    @Override
    public String toString() {
        return "§" + this.code;
    }
}