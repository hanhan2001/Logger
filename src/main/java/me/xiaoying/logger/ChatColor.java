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

    public static char getCodeByInt(int code) {
        switch (code) {
            case 0:
                return '0';
            case 1:
                return '1';
            case 2:
                return '2';
            case 3:
                return '3';
            case 4:
                return '4';
            case 5:
                return '5';
            case 6:
                return '6';
            case 7:
                return '7';
            case 8:
                return '8';
            case 9:
                return '9';
            case 10:
                return 'A';
            case 11:
                return 'B';
            case 12:
                return 'C';
            case 13:
                return 'D';
            case 14:
                return 'E';
            case 15:
                return 'F';
        }

        return 0;
    }

    public static short getShortCode(char code) {
        switch (Character.toLowerCase(code)) {
            case '0':
                return 0x0;
            case '1':
                return 0x1;
            case '2':
                return 0x2;
            case '3':
                return 0x3;
            case '4':
                return 0x4;
            case '5':
                return 0x5;
            case '6':
                return 0x6;
            case '7':
                return 0x7;
            case '8':
                return 0x8;
            case '9':
                return 0x9;
            case 'a':
                return 0xA;
            case 'b':
                return 0xB;
            case 'c':
                return 0xC;
            case 'd':
                return 0xD;
            case 'e':
                return 0xE;
            case 'f':
                return 0xF;
        }
        return 0x0;
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
            if (chars[i] != altCharColor || "123456789AaBbCcDdEeFf".indexOf(chars[i + 1]) == -1)
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