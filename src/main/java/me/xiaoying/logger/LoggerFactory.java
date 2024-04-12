package me.xiaoying.logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPOutputStream;

public class LoggerFactory {
    File logPath = new File("./logs/latest.log");
    private JNILogger jniLogger;

    public LoggerFactory() {
        this.jniLogger = new JNILogger();
    }

    public Logger getLogger() {
        return new Logger(this);
    }

    public Logger getLogger(Class<?> clazz) {
        return new Logger(clazz, this);
    }

    public void log(String message) {
        message = ChatColor.stripColor(message);

        if (!this.logPath.getParentFile().exists())
            this.logPath.getParentFile().mkdirs();

        try {
            if (!this.logPath.exists())
                this.logPath.createNewFile();

            Files.writeString(Paths.get(this.logPath.getPath()), message + "\n", StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveLog() {
        try {
            File file = new File(this.logPath.getParent() + "/" + new VariableFactory("%date%.log.gz").date("yyyy-MM-dd"));
            for (int i = 1; file.exists(); i++)
                file = new File(this.logPath.getParent() + "/" + new VariableFactory("%date%-" + i + ".log.gz").date("yyyy-MM-dd"));

            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(new FileOutputStream(file));
            FileInputStream fileInputStream = new FileInputStream(this.logPath);
            byte[] buf = new byte[1024];
            int len;
            while ((len = fileInputStream.read(buf)) > 0)
                gzipOutputStream.write(buf, 0, len);

            fileInputStream.close();
            gzipOutputStream.finish();
            gzipOutputStream.close();

            this.logPath.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JNILogger getJniLogger() {
        return this.jniLogger;
    }

    private class VariableFactory {
        private String string;

        public VariableFactory(String string) {
            this.string = string;
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