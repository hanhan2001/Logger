package me.xiaoying.logger;

import me.xiaoying.logger.event.EventHandle;
import me.xiaoying.logger.event.terminal.TerminalLogEndEvent;
import me.xiaoying.logger.event.terminal.TerminalWantLogEvent;
import me.xiaoying.logger.printstream.LErrorPrintStream;
import me.xiaoying.logger.printstream.LOutPrintStream;

import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;

public class LoggerFactory {
    private static final JNILogger jniLogger = new JNILogger();
    private static File logFile = new File("./logs/latest.log");
    private static String defaultName = logFile.getParent() + "/" + new VariableFactory("%date%.log.gz").date("yyyy-MM-dd");
    private static String conflictName = logFile.getParent() + "/" + new VariableFactory("%date%-%i%.log.gz").date("yyyy-MM-dd");
    private static boolean needSave = false;

    private static CopyOnWriteArrayList<Message> messageQueue = new CopyOnWriteArrayList<>();

    private static final LOutPrintStream out = new LOutPrintStream(System.out);
    private static final LErrorPrintStream err = new LErrorPrintStream(System.out);

    static {
        System.setOut(LoggerFactory.out);
        System.setErr(LoggerFactory.err);

        if (LoggerFactory.logFile.exists()) save();

        new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.MICROSECONDS.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                for (int i = 0; i < LoggerFactory.messageQueue.size(); i++) {
                    Message message = LoggerFactory.messageQueue.get(i);

                    if (message.isCallEvent()) EventHandle.callEvent(new TerminalWantLogEvent());

                    message.getJniLogger().send(message.getMessage(), message.getAltCharColor(), message.isNewLine());
                    LoggerFactory.log(message.getMessage());

                    if (message.isCallEvent()) EventHandle.callEvent(new TerminalLogEndEvent());

                    LoggerFactory.messageQueue.remove(i--);
                }
            }
        }).start();
    }

    public static void setLogFile(String file) {
        logFile = new File(file);
    }

    public static void setLogFile(File file) {
        logFile = file;
    }

    public static File getLogFile() {
        return logFile;
    }

    public static String getDefaultName() {
        return defaultName;
    }

    public static void setDefaultName(String name) {
        defaultName = name;
    }

    public static String getConflictName() {
        return conflictName;
    }

    public static void setConflictName(String name) {
        conflictName = name;
    }

    public static Logger getLogger() {
        return new Logger();
    }

    public static Logger getLogger(Class<?> clazz) {
        return new Logger(clazz);
    }

    public static void save() {
        try {
            File file = new File(new VariableFactory(defaultName).date("yyyy-MM-dd").toString());
            for (int i = 1; file.exists(); i++)
                file = new File(new VariableFactory(conflictName).i(i).date("yyyy-MM-dd").toString());

            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(Files.newOutputStream(file.toPath()));
            FileInputStream fileInputStream = new FileInputStream(logFile);
            byte[] buf = new byte[1024];
            int len;
            while ((len = fileInputStream.read(buf)) > 0)
                gzipOutputStream.write(buf, 0, len);

            fileInputStream.close();
            gzipOutputStream.finish();
            gzipOutputStream.close();

            logFile.delete();
        } catch (Exception e) {

        }
    }

    public void setNeedSave(boolean bool) {
        needSave = bool;
    }

    public static boolean needSave() {
        return needSave;
    }

    public static JNILogger getJniLogger() {
        return LoggerFactory.jniLogger;
    }

    public static void addMessage(String message, String altCharColor, boolean newLine, boolean callEvent, JNILogger logger) {
        try {
            LoggerFactory.messageQueue.add(new Message(message, altCharColor, newLine, callEvent, logger));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void log(String message) {
        if (LoggerFactory.needSave())
            return;

        message = ChatColor.stripColor(message);

        if (!LoggerFactory.getLogFile().getParentFile().exists())
            LoggerFactory.getLogFile().getParentFile().mkdirs();

        try {

            if (!LoggerFactory.getLogFile().exists())
                LoggerFactory.getLogFile().createNewFile();

            BufferedWriter writer = new BufferedWriter(new FileWriter(LoggerFactory.getLogFile().getPath(), true));
            if (!message.endsWith("\n"))
                writer.write(message + "\n");
            else
                writer.write(message);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static LOutPrintStream getOut() {
        return LoggerFactory.out;
    }

    public static LErrorPrintStream getErr() {
        return LoggerFactory.err;
    }

    private static class VariableFactory {
        private String string;

        public VariableFactory(String string) {
            this.string = string;
        }

        public VariableFactory i(int i) {
            this.string = this.string.replace("%i%", String.valueOf(i));
            return this;
        }

        public VariableFactory date(String format) {
            this.string = this.string.replace("%date%", new SimpleDateFormat(format).format(new Date()));
            return this;
        }

        @Override
        public String toString() {
            return this.string;
        }
    }

    private static class Message {
        private final String message;
        private final String altCharColor;
        private final boolean newLine;
        private final boolean callEvent;
        private final JNILogger jniLogger;

        public Message(String message, String altCharColor, boolean newLine, boolean callEvent, JNILogger jniLogger) {
            this.message = message;
            this.altCharColor = altCharColor;
            this.newLine = newLine;
            this.callEvent = callEvent;
            this.jniLogger = jniLogger;
        }

        public String getMessage() {
            return this.message;
        }

        public String getAltCharColor() {
            return this.altCharColor;
        }

        public boolean isNewLine() {
            return this.newLine;
        }

        public boolean isCallEvent() {
            return this.callEvent;
        }

        public JNILogger getJniLogger() {
            return this.jniLogger;
        }
    }
}