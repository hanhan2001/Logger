package me.xiaoying.logger;

import me.xiaoying.logger.printsrteam.LPrintStream;
import me.xiaoying.logger.render.LinuxRender;
import me.xiaoying.logger.render.Render;
import me.xiaoying.logger.render.WinRender;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LoggerFactory {
//    private static final Map<Class<?>, Logger> loggers = new HashMap<>();
    private static final Map<String, Logger> loggers = new HashMap<>();

    private static Render render;

    static {
        System.setOut(new LPrintStream(System.out));
        System.setErr(new LPrintStream(System.err));

        if (System.getProperty("os.name").toUpperCase(Locale.ENGLISH).startsWith("WINDOWS"))
            LoggerFactory.render = new WinRender();
        else
            LoggerFactory.render = new LinuxRender();
    }

    /**
     * 获取无 class 的 Logger
     *
     * @return Logger
     */
    public static Logger getLogger() {
        return new Logger();
    }

    /**
     * 获取 Logger
     *
     * @param clazz 指定来源 class(也可当作 Logger 名称使用)
     * @return Logger
     */
    public static Logger getLogger(String clazz) {
        Logger logger;

        if ((logger = LoggerFactory.loggers.get(clazz)) == null) {
            logger = new Logger(clazz);
            LoggerFactory.loggers.put(clazz, logger);
        }

        return logger;
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
            LoggerFactory.loggers.put(clazz.toString(), logger);
        }

        return logger;
    }

    /**
     * 获取渲染器<br>
     * 其实只是输出字符的处理器
     *
     * @return Render
     */
    public static Render getRender() {
        return LoggerFactory.render;
    }
}