package me.xiaoying.logger;

import me.xiaoying.logger.printstream.LErrorPrintStream;
import me.xiaoying.logger.printstream.LOutPrintStream;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;

public class LoggerFactory {
    private static final JNILogger jniLogger = new JNILogger();
    private static File logFile = new File("./logs/latest.log");
    private static String defaultName = logFile.getParent() + "/" + new VariableFactory("%date%.log.gz").date("yyyy-MM-dd");
    private static String conflictName = logFile.getParent() + "/" + new VariableFactory("%date%-%i%.log.gz").date("yyyy-MM-dd");
    private static boolean needSave = false;

    private static boolean logging = false;
    private static Stack<Message> messageQueue = new Stack<>();

    static {
        System.setOut(new LOutPrintStream(System.out));
        System.setErr(new LErrorPrintStream(System.out));

        if (LoggerFactory.logFile.exists()) save();

        new Thread(() -> {
            while (true) {
                try {
                    try {
                        TimeUnit.MICROSECONDS.sleep(50);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    Iterator<Message> iterator = LoggerFactory.messageQueue.iterator();

                    Message message;

                    while (iterator.hasNext() && (message = iterator.next()) != null) {
                        message.getJniLogger().send(message.getMessage(), message.getAltCharColor());
                        iterator.remove();
                    }
                } catch (Exception e) {
                    // ╮（╯＿╰）╭
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

            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(new FileOutputStream(file));
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

    public static void addMessage(String message, String altCharColor, JNILogger logger) {
        try {
            LoggerFactory.messageQueue.add(new Message(message, altCharColor, logger));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        private final JNILogger jniLogger;

        public Message(String message, String altCharColor, JNILogger jniLogger) {
            this.message = message;
            this.altCharColor = altCharColor;
            this.jniLogger = jniLogger;
        }

        public String getMessage() {
            return this.message;
        }

        public String getAltCharColor() {
            return this.altCharColor;
        }

        public JNILogger getJniLogger() {
            return this.jniLogger;
        }
    }
}