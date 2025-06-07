package me.xiaoying.logger;

import java.util.HashMap;
import java.util.Map;

public class LoggerFactory {
    private static final Map<Class<?>, Logger> loggers = new HashMap<>();

    private static boolean nextLine = false;

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
     * 设置是否需要换行
     *
     * @param nextLine 是否需要换行
     */
    protected static void setNextLine(boolean nextLine) {
        LoggerFactory.nextLine = nextLine;
    }

    /**
     * 判断是否需要换行
     *
     * @return 是否需要换行
     */
    protected static boolean nextLine() {
        return LoggerFactory.nextLine;
    }
}