package me.xiaoying.logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoggerFactory {
    private static final Map<Class<?>, Logger> loggers = new HashMap<>();

    private static String dateFormat = "yyyy/MM/dd-HH:mm:ss";
    private static String loggerFormat = null;
    private static Map<Logger.Level, String> loggerFormats = new HashMap<>();

    static {
        LoggerFactory.loggerFormats.put(Logger.Level.INFO, "");
        LoggerFactory.loggerFormats.put(Logger.Level.WARN, "");
        LoggerFactory.loggerFormats.put(Logger.Level.ERROR, "");
        LoggerFactory.loggerFormats.put(Logger.Level.DEBUG, "");
    }

    /**
     * 获取 Logger
     *
     * @param clazz 指定来源 Class
     * @return 有 class 对象的 Logger
     */
    public static Logger getLogger(Class<?> clazz) {
        Logger logger;

        if ((logger = LoggerFactory.loggers.get(clazz)) == null) {
            logger = new Logger(clazz);
            LoggerFactory.loggers.put(clazz, logger);
        }

        return logger;
    }

    /**
     * 获取统一的日志格式<br>
     * 如果未设置统一的日志格式则返回 null
     *
     * @return 日志格式
     */
    public static String getFormat() {
        return LoggerFactory.loggerFormat;
    }

    /**
     * 获取不同 Level 的日志格式
     *
     * @param level Logger Level
     * @return 对应 Level 的日志格式
     */
    public static String getFormat(Logger.Level level) {
        return LoggerFactory.loggerFormats.get(level);
    }

    /**
     * 获取 %date% 变量的格式
     *
     * @return Date变量格式
     */
    public static String getDateFormat() {
        return LoggerFactory.dateFormat;
    }

    public static void unset() {
        LoggerFactory.loggerFormat = null;
        LoggerFactory.dateFormat = "yyyy/MM/dd-HH:mm:ss";
    }
}