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

    private char code;
    private int intCode;

    ChatColor(char code, int intCode) {
        this.code = code;
        this.intCode = intCode;
    }

    public static String translateAlternateColorCodes(char altCharColor, String text) {
        char[] b = text.toCharArray();

        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] != altCharColor || "123456789AaBbCcDdEeFf".indexOf(b[i + 1]) == -1)
                continue;

            b[i] = 38;
            b[i + 1] = Character.toLowerCase(b[i + 1]);
        }
        return new String(b);
    }

    public char getChar() {
        return this.code;
    }

    @Override
    public String toString() {
        return "§" + this.code;
    }
}