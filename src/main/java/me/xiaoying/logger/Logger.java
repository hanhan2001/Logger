package me.xiaoying.logger;

import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import me.xiaoying.logger.utils.ColorUtil;

import java.util.HashMap;
import java.util.Map;

public class Logger {
    // 所有 Level 的输出格式
    // 如果 formats 中不存在同 Level 的 format 格式则会调用此格式
    private String format;
    private final Map<Level, String> formats = new HashMap<>();

    // %date% 变量格式
    private String dateFormat = "yyyy/MM/dd-HH:mm:ss";

    private Class<?> clazz = null;

    protected Logger() {
        this.format = "[%date%] [%level%] - %message%";
    }

    protected Logger(Class<?> clazz) {
        this.clazz = clazz;

        this.format = "[%date%] [%level%] [%class%] - %message%";
    }

    public void info(String message) {
        this.info(message, new Object[0]);
    }

    public void info(String message, Object... args) {
        this.logger(new VariableFactory(this.format(Level.INFO, message)).clazz(this.clazz).date(this.dateFormat).level(Level.INFO).parameters(args).color().toString());
    }

    public void warn(String message) {
        this.info(message, new Object[0]);
    }

    public void warn(String message, Object... args) {
        this.logger(new VariableFactory(this.format(Level.WARN, message)).clazz(this.clazz).date(this.dateFormat).level(Level.WARN).parameters(args).color().toString());
    }

    public void error(String message) {
        this.error(message, new Object[0]);
    }

    public void error(String message, Object... args) {
        this.logger(new VariableFactory(this.format(Level.ERROR, message)).clazz(this.clazz).date(this.dateFormat).level(Level.ERROR).parameters(args).color().toString());
    }

    public void debug(String message) {
        this.error(message, new Object[0]);
    }

    public void debug(String message, Object... args) {
        this.logger(new VariableFactory(this.format(Level.DEBUG, message)).clazz(this.clazz).date(this.dateFormat).level(Level.DEBUG).parameters(args).color().toString());
    }

    private void logger(String message) {
        if (LoggerFactory.nextLine())
            message = "\n" + message;

        Kernel32Ex kernel32 = Kernel32Ex.INSTANCE;

        WinNT.HANDLE hConsole = kernel32.GetStdHandle(Kernel32Ex.STD_OUTPUT_HANDLE);

        Kernel32Ex.CONSOLE_SCREEN_BUFFER_INFO consoleInfo = new Kernel32Ex.CONSOLE_SCREEN_BUFFER_INFO();
        kernel32.GetConsoleScreenBufferInfo(hConsole, consoleInfo);

        int originColor = consoleInfo.wAttributes;
        int originForeColor = originColor & 0x0F;
        int originBackColor = originColor & 0xF0;

        try {
            char[] chars = message.toCharArray();

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


                    code = chars[i + 1] == 'r' ? originForeColor : ChatColor.getShortCode(chars[i + 1]);

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

    /**
     * 获取通用输出格式
     *
     * @return 通用输出格式
     */
    public String getFormat() {
        return this.format;
    }

    /**
     * 获取指定 Level 的输出格式
     *
     * @param level 指定 Level
     * @return Level 的输出格式
     */
    public String getFormat(Level level) {
        String string = this.formats.get(level);

        if (string == null || string.isEmpty())
            string = this.format;

        return string;
    }

    /**
     * 获取 %date% 变量格式
     *
     * @return %date% 格式
     */
    public String getDateFormat() {
        return this.dateFormat;
    }

    /**
     * 设置统一的输出格式<br>
     * 如果 Level 存在指定的输出格式则此处设置无效<br>
     * 可以通过 {@code Logger#setFormat} 设置对应的 Level 输出格式为 null 则此处设置有效
     *
     * @param format 输出格式
     * @return Logger
     */
    public Logger setFormat(String format) {
        this.format = format;
        return this;
    }

    /**
     * 设置对应 Level 的输出格式
     *
     * @param level  Level
     * @param format 输出格式
     * @return Logger
     */
    public Logger setFormat(Level level, String format) {
        this.formats.put(level, format);
        return this;
    }

    /**
     * 设置 %date% 变量格式
     *
     * @param dateFormat 日期格式
     * @return Logger
     */
    public Logger setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
        return this;
    }

    private String format(Level level, String message) {
        if (this.getFormat(level) == null)
            message = new VariableFactory(this.format).message(message).toString();
        else
            message = new VariableFactory(this.getFormat()).message(message).toString();

        return message;
    }

    public enum Level {
        INFO('a'),
        WARN('e'),
        ERROR('c'),
        DEBUG('b');

        private final char color;

        Level(char color) {
            this.color = color;
        }

        public char getColor() {
            return this.color;
        }

        @Override
        public String toString() {
            return "§" + this.color + super.toString() + "§r";
        }
    }
}