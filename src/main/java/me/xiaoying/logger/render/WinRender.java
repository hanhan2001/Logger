package me.xiaoying.logger.render;

import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import me.xiaoying.logger.LoggerFactory;
import me.xiaoying.logger.utils.ColorUtil;

public class WinRender implements Render {
    @Override
    public void render(String string) {
        Kernel32Ex kernel32 = Kernel32Ex.INSTANCE;

        WinNT.HANDLE hConsole = kernel32.GetStdHandle(Kernel32Ex.STD_OUTPUT_HANDLE);

        Kernel32Ex.CONSOLE_SCREEN_BUFFER_INFO consoleInfo = new Kernel32Ex.CONSOLE_SCREEN_BUFFER_INFO();
        kernel32.GetConsoleScreenBufferInfo(hConsole, consoleInfo);

        int originColor = consoleInfo.wAttributes;
        int originForeColor = originColor & 0x0F;
        int originBackColor = originColor & 0xF0;

        try {
            char[] chars = string.toCharArray();

            StringBuilder stringBuilder = new StringBuilder();

            int code = originForeColor;

            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == '§') {
                    if (i + 1 > chars.length - 1 || !ColorUtil.isColorKey(chars[i + 1])) {
                        stringBuilder.append(chars[i]).append(chars[i + 1]);
                        i += 1;
                        continue;
                    }

                    char[] charArray = stringBuilder.toString().toCharArray();

                    kernel32.SetConsoleTextAttribute(kernel32.GetStdHandle(Kernel32Ex.STD_OUTPUT_HANDLE), code & 0x0F | originBackColor);
                    kernel32.WriteConsoleW(hConsole, charArray, charArray.length, new IntByReference(0), null);

                    code = chars[i + 1] == 'r' ? originForeColor : this.getShortCode(chars[i + 1]);

                    i += 1;
                    stringBuilder.delete(0, stringBuilder.length());
                    continue;
                }

                stringBuilder.append(chars[i]);
            }

            if (stringBuilder.length() > 0) {
                char[] charArray = stringBuilder.toString().toCharArray();
                kernel32.SetConsoleTextAttribute(kernel32.GetStdHandle(Kernel32Ex.STD_OUTPUT_HANDLE), code & 0x0F | originBackColor);
                kernel32.WriteConsoleW(hConsole, charArray, charArray.length, new IntByReference(0), null);
            }

        } finally {
            LoggerFactory.setNextLine(true);
            kernel32.SetConsoleTextAttribute(hConsole, originColor);
        }
    }

    private short getShortCode(char code) {
        switch (Character.toLowerCase(code)) {
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
        return 0xF;
    }
}