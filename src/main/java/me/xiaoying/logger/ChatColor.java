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
    private static String altCharColor = "&";

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

    public static String stripColor(String text) {
        if (!text.contains(altCharColor))
            return text;

        StringBuilder stringBuilder = new StringBuilder();

        String[] split = text.split(altCharColor);
        for (int i = 0; i < split.length; i++) {
            if (i == 0) {
                stringBuilder.append(split[i]);
                continue;
            }

            if (split[i].length() == 1) {
                if (!"123456789AaBbCcDdEeFf".contains(split[i]))
                    stringBuilder.append(altCharColor).append(split[i]);
                continue;
            }

            if ("123456789AaBbCcDdEeFf".contains(split[i].substring(0, 1))) {
                stringBuilder.append(split[i].substring(1, split[i].length()));
                continue;
            }

            stringBuilder.append(altCharColor).append(split[i]);
        }
        StringBuilder builder = new StringBuilder(altCharColor);
        while (true) {
            if (text.endsWith(builder.toString())) {
                builder.append(altCharColor);
                continue;
            }
            builder.replace(builder.length() - 1, builder.length(), "");
            break;
        }
        stringBuilder.append(builder);
        return stringBuilder.toString();
    }

    public static void setAltCharColor(char altCharColor) {
        ChatColor.altCharColor = String.valueOf(altCharColor); 
    }
    
    public static String getAltCharColor() {
        return altCharColor;
    }
    
    @Override
    public String toString() {
        return altCharColor + this.code;
    }
}