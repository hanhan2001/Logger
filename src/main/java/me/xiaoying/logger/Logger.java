package me.xiaoying.logger;

import me.xiaoying.logger.event.EventHandle;
import me.xiaoying.logger.event.terminal.TerminalWantLogEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    Class<?> clazz;
    String format;
    LoggerFactory loggerFactory;
    JNILogger jniLogger;
    String dateFormat = "yyyy/MM/dd-HH:mm:ss";

    public Logger(LoggerFactory loggerFactory) {
        this.format = "[%date%] [%level%] - %message%";
        this.loggerFactory = loggerFactory;
        this.jniLogger = this.loggerFactory.getJniLogger();
    }

    public Logger(Class<?> clazz, LoggerFactory loggerFactory) {
        this.clazz = clazz;
        this.format = "[%date%] [%level%] - [%class%]: %message%";
        this.loggerFactory = loggerFactory;
        this.jniLogger = this.loggerFactory.getJniLogger();
    }

    public Logger setFormat(String format) {
        if (format == null || format.isEmpty())
            return this;

        this.format = format;
        return this;
    }

    public String getFormat() {
        return this.format;
    }

    public Logger setDateFormat(String dateFormat) {
        if (format == null || format.isEmpty())
            return this;

        this.dateFormat = dateFormat;
        return this;
    }

    public String getDateFormat() {
        return this.dateFormat;
    }

    public void info(String message, String... strings) {
        EventHandle.callEvent(new TerminalWantLogEvent());
        message = this.parameter(message, strings);

        String string = new VariableFactory(this.format).date(this.dateFormat).message(message).clazz(this.clazz).level("&aINFO&f").toString();
        this.jniLogger.send(string, "&");
        this.loggerFactory.log(string);
    }

    public void warn(String message, String... strings) {
        EventHandle.callEvent(new TerminalWantLogEvent());
        message = this.parameter(message, strings);

        String string = new VariableFactory(this.format).date(this.dateFormat).message(message).clazz(this.clazz).level("&eWARN&f").toString();
        this.jniLogger.send(string, "&");
        this.loggerFactory.log(string);
    }

    public void error(String message, String... strings) {
        EventHandle.callEvent(new TerminalWantLogEvent());
        message = this.parameter(message, strings);

        String string = new VariableFactory(this.format).date(this.dateFormat).message(message).clazz(this.clazz).level("&cERROR&f").toString();
        this.jniLogger.send(string, "&");
        this.loggerFactory.log(string);
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

        public VariableFactory date(String format) {
            this.string = this.string.replace("%date%", new SimpleDateFormat(format).format(new Date()));
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