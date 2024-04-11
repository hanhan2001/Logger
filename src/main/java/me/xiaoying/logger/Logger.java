package me.xiaoying.logger;

public class Logger {
    Class<?> clazz;
    String format;
    LoggerFactory loggerFactory;
    JNILogger jniLogger;

    public Logger(LoggerFactory loggerFactory) {
        this.format = "[%level%] - %message%";
        this.loggerFactory = loggerFactory;
        this.jniLogger = this.loggerFactory.getJniLogger();
    }

    public Logger(Class<?> clazz, LoggerFactory loggerFactory) {
        this.clazz = clazz;
        this.format = "[%level%] - [%class%]: %message%";
        this.loggerFactory = loggerFactory;
        this.jniLogger = this.loggerFactory.getJniLogger();
    }

    public void setFormat(String format) {
        if (format == null || format.isEmpty())
            return;

        this.format = format;
    }

    public String getFormat() {
        return this.format;
    }

    public void info(String message, String... strings) {
        message = this.parameter(message, strings);

        this.jniLogger.send(new VariableFactory(this.format).message(message).clazz(this.clazz).level("&aINFO&f").toString(), "&");
    }

    public void warn(String message, String... strings) {
        message = this.parameter(message, strings);
        this.jniLogger.send(new VariableFactory(this.format).message(message).clazz(this.clazz).level("&eWARN&f").toString(), "&");
    }

    public void error(String message, String... strings) {
        message = this.parameter(message, strings);
        this.jniLogger.send(new VariableFactory(this.format).message(message).clazz(this.clazz).level("&cERROR&f").toString(), "&");
    }

    private String parameter(String message, String... strings) {
        int time = 0;
        while (message.contains("{}") && time < strings.length) {
            message = message.replaceFirst("\\{\\}", strings[time]);
            time++;
        }
        return message;
    }

    private class VariableFactory {
        private String string;

        public VariableFactory(String string) {
            this.string = string;
        }

        public VariableFactory clazz(Class<?> clacc) {
            if (clacc == null)
                return this;

            this.string = this.string.replace("%class%", clacc.getName());
            return this;
        }

        public VariableFactory level(String level) {
            this.string = this.string.replace("%level%", level);
            return this;
        }

        public VariableFactory message(String message) {
            this.string = this.string.replace("%message%", message);
            return this;
        }

        @Override
        public String toString() {
            return this.string;
        }
    }
}