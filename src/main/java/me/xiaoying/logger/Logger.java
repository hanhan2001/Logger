package me.xiaoying.logger;

import me.xiaoying.logger.event.EventHandle;
import me.xiaoying.logger.event.log.LogEndEvent;
import me.xiaoying.logger.event.log.PrepareLogEvent;

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

    public void info(Object message) {
        this.info(message, new Object[0]);
    }

    public void info(Object message, Object... args) {
        if (message == null)
            return;

        this.logger(new VariableFactory(message.toString()).parameters(args).color().toString(), Level.INFO);
    }

    public void warn(Object message) {
        this.info(message, new Object[0]);
    }

    public void warn(Object message, Object... args) {
        if (message == null)
            return;

        this.logger(new VariableFactory(message.toString()).parameters(args).color().toString(), Level.WARN);
    }

    public void error(Object message) {
        this.error(message, new Object[0]);
    }

    public void error(Object message, Object... args) {
        if (message == null)
            return;

        this.logger(new VariableFactory(message.toString()).parameters(args).color().toString(), Level.ERROR);
    }

    public void debug(Object message) {
        this.error(message, new Object[0]);
    }

    public void debug(Object message, Object... args) {
        if (message == null)
            return;

        this.logger(new VariableFactory(message.toString()).parameters(args).color().toString(), Level.DEBUG);
    }

    /**
     * 类似直接打印的效果，打印出来的内容不会被 EventHandler 捕捉到
     *
     * @param message 内容
     */
    public void print(String message) {
        String originFormat = this.format;

        this.setFormat("%message%");
        LoggerFactory.getRender().render(message);

        this.setFormat(originFormat);
    }

    /**
     * 类似直接打印的效果，打印出来的内容不会被 EventHandler 捕捉到
     *
     * @param message 内容
     */
    public void println(String message) {
        this.print(message + "\n");
    }

    private void logger(String message, Level level) {
        String origin = message;

        // 触发 PrepareLogEvent
        PrepareLogEvent event = new PrepareLogEvent(this, origin);
        EventHandle.callEvent(event);

        // 当此事件被 cancel 时则不会打印消息内容
        if (event.isCanceled())
            return;

        // 同步事件中设置的 message 内容
        message = event.getMessage();
        message = new VariableFactory(this.format(level, String.valueOf(message))).clazz(this.clazz).date(this.dateFormat).level(level).color().toString();

        origin = message;

        message = message + "\n";

        LoggerFactory.getRender().render(message);

        // 触发 LogEndEvent 事件
        EventHandle.callEvent(new LogEndEvent(origin));
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