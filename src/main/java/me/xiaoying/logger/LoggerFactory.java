package me.xiaoying.logger;

import me.xiaoying.logger.terminal.LPrintStream;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPOutputStream;

public class LoggerFactory {
    private static final JNILogger jniLogger = new JNILogger();
    private static File logFile = new File("./logs/latest.log");
    private static String defaultName = logFile.getParent() + "/" + new VariableFactory("%date%.log.gz").date("yyyy-MM-dd");
    private static String conflictName = logFile.getParent() + "/" + new VariableFactory("%date%-%i%.log.gz").date("yyyy-MM-dd");

    static {
        System.setOut(new LPrintStream(System.out));
        System.setErr(new LPrintStream(System.out));

        if (logFile.exists()) save();
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
            e.printStackTrace();
        }
    }

    public static JNILogger getJniLogger() {
        return jniLogger;
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
}