package me.xiaoying.logger.render;

import me.xiaoying.logger.LoggerFactory;
import me.xiaoying.logger.utils.ColorUtil;

import java.nio.charset.StandardCharsets;

public class LinuxRender implements Render {
    @Override
    public void render(String string) {
        char[] chars = string.toCharArray();

        StringBuilder stringBuilder = new StringBuilder();
        String color = "\033[97m";

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '§') {
                if (i + 1 > chars.length - 1 || !ColorUtil.isColorKey(chars[i + 1])) {
                    stringBuilder.append(chars[i]).append(chars[i + 1]);
                    i += 1;
                    continue;
                }

                byte[] bytes = (color + stringBuilder).getBytes(StandardCharsets.UTF_8);
                CLibrary.INSTANCE.write(1, bytes, bytes.length);

                color = this.getColor(chars[i + 1]);

                i += 1;
                stringBuilder.delete(0, stringBuilder.length());
                continue;
            }

            stringBuilder.append(chars[i]);
        }

        if (stringBuilder.length() > 0) {
            byte[] bytes = (color + stringBuilder).getBytes(StandardCharsets.UTF_8);
            CLibrary.INSTANCE.write(1, bytes, bytes.length);
        }

        LoggerFactory.setNextLine(true);
    }

    public String getColor(char code) {
        switch (code) {
            case '1':
                return "\033[34m";
            case '2':
                return "\033[32m";
            case '3':
                return "\033[94m";
            case '4':
                return "\033[31m";
            case '5':
                return "\033[35m";
            case '6':
                return "\033[33m";
            case '7':
                return "\033[37m";
            case '8':
                return "\033[90m";
            case '9':
                return "\033[36m";
            case 'a':
                return "\033[92m";
            case 'b':
                return "\033[96m";
            case 'c':
                return "\033[91m";
            case 'd':
                return "\033[95m";
            case 'e':
                return "\033[93m";
            case 'f':
                return "\033[97m";

        }

        return "\033[97m";
    }
}