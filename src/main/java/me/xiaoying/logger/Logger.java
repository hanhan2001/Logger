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

    public void info(String message, String... strings) {
        EventHandle.callEvent(new TerminalWantLogEvent());
        message = this.parameter(message, strings);

        String string = new VariableFactory(this.format).date(this.dateFormat).message(message).clazz(this.clazz).level("&aINFO&f").toString();
        this.jniLogger.send(string, "&");
        this.log(string);
        EventHandle.callEvent(new TerminalLogEndEvent());
    }

    public void warn(String message, String... strings) {
        EventHandle.callEvent(new TerminalWantLogEvent());
        message = this.parameter(message, strings);

        String string = new VariableFactory(this.format).date(this.dateFormat).message(message).clazz(this.clazz).level("&eWARN&f").toString();
        this.jniLogger.send(string, "&");
        this.log(string);
        EventHandle.callEvent(new TerminalLogEndEvent());
    }

    public void error(String message, String... strings) {
        EventHandle.callEvent(new TerminalWantLogEvent());
        message = this.parameter(message, strings);

        String string = new VariableFactory(this.format).date(this.dateFormat).message(message).clazz(this.clazz).level("&cERROR&f").toString();
        this.jniLogger.send(string, "&");
        this.log(string);
        EventHandle.callEvent(new TerminalLogEndEvent());
    }

    private String parameter(String message, String... strings) {
        for (String string : strings) {
            if (!message.contains("{}"))
                return message;

            message = message.replaceFirst("\\{}", string);
        }
        return message;
    }
    
    private void log(String message) {
        message = ChatColor.stripColor(message);

        if (!LoggerFactory.getLogFile().getParentFile().exists())
            LoggerFactory.getLogFile().getParentFile().mkdirs();

        try {

            if (!LoggerFactory.getLogFile().exists())
                LoggerFactory.getLogFile().createNewFile();

            BufferedWriter writer = new BufferedWriter(new FileWriter(LoggerFactory.getLogFile().getPath(), true));
            writer.write(message + "\n");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final class VariableFactory {
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