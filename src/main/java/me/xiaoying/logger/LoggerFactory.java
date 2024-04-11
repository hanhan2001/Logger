package me.xiaoying.logger;

public class LoggerFactory {
    private JNILogger jniLogger;

    public LoggerFactory() {
        this.jniLogger = new JNILogger();
    }

    public Logger getLogger(Class<?> clazz) {
        return new Logger(clazz, this);
    }

    public JNILogger getJniLogger() {
        return this.jniLogger;
    }
}