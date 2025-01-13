package me.xiaoying.logger;

import me.xiaoying.logger.event.EventHandle;
import me.xiaoying.logger.event.terminal.TerminalLogEndEvent;
import me.xiaoying.logger.event.terminal.TerminalWantLogEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private Class<?> clazz;
    private String format;
    private JNILogger jniLogger;
    private String dateFormat = "yyyy/MM/dd-HH:mm:ss";

    public Logger() {
        this.format = "[%date%] [%level%] - %message%";
        this.jniLogger = LoggerFactory.getJniLogger();
    }

    public Logger(Class<?> clazz) {
        this.clazz = clazz;
        this.format = "[%date%] [%level%] - [%class%]: %message%";
        this.jniLogger = LoggerFactory.getJniLogger();
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

    public void print(String message, Object... objects) {
        message = this.parameter(message, objects);

        String string = new VariableFactory("%message%").message(message).toString();
        LoggerFactory.addMessage(string, "&", false, false, this.jniLogger);
    }

    public void println(String message, Object... objects) {
        this.print(message + "\n", objects);
    }

    public void info(String message, Object... objects) {
        message = this.parameter(message, objects);

        String string = new VariableFactory(this.format).date(this.dateFormat).message(message).clazz(this.clazz).level("&aINFO&f").toString();
        LoggerFactory.addMessage(string, "&", true, true, this.jniLogger);
    }

    public void warn(String message, Object... objects) {
        message = this.parameter(message, objects);

        String string = new VariableFactory(this.format).date(this.dateFormat).message(message).clazz(this.clazz).level("&eWARN&f").toString();
        LoggerFactory.addMessage(string, "&", true, true, this.jniLogger);
    }

    public void error(String message, Object... objects) {
        message = this.parameter(message, objects);

        String string = new VariableFactory(this.format).date(this.dateFormat).message(message).clazz(this.clazz).level("&cERROR&f").toString();
        LoggerFactory.addMessage(string, "&", true, true, this.jniLogger);
    }

    public void debug(String message, Object... objects) {
        message = this.parameter(message, objects);

        String string = new VariableFactory(this.format).date(this.dateFormat).message(message).clazz(this.clazz).level("&bDEBUG&f").toString();
        LoggerFactory.addMessage(string, "&", true, true, this.jniLogger);
    }

    private String parameter(String message, Object... objects) {
        if (!message.contains("{}"))
            return message;

        for (Object object : objects) {
            if (!message.contains("{}"))
                return message;

            message = message.replaceFirst("\\{}", object.toString());
        }
        return message;
    }

    private static final class VariableFactory {
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