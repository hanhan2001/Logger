package me.xiaoying.logger;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPOutputStream;

public class LoggerFactory {
    private static JNILogger jniLogger;
    private static File logFile = new File("./logs/latest.log");
    private static String defaultName;
    private static String conflictName;

    public LoggerFactory() {
        defaultName = logFile.getParent() + "/" + new VariableFactory("%date%.log.gz").date("yyyy-MM-dd");
        conflictName = logFile.getParent() + "/" + new VariableFactory("%date%-%i%.log.gz").date("yyyy-MM-dd");

        jniLogger = new JNILogger();

        if (logFile.exists())
            this.save();
    }

    public void setLogFile(String file) {
        logFile = new File(file);
    }

    public void setLogFile(File file) {
        logFile = file;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String name) {
        defaultName = name;
    }

    public String getConflictName() {
        return conflictName;
    }

    public void setConflictName(String name) {
        conflictName = name;
    }

    public Logger getLogger() {
        return new Logger(this);
    }

    public Logger getLogger(Class<?> clazz) {
        return new Logger(clazz, this);
    }

    public void log(String message) {
        message = ChatColor.stripColor(message);

        if (!logFile.getParentFile().exists())
            logFile.getParentFile().mkdirs();

        try {
            if (!logFile.exists())
                logFile.createNewFile();

            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile.getPath(), true));
            writer.write(message + "\n");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
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

    public JNILogger getJniLogger() {
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