package me.xiaoying.logger;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPOutputStream;

public class LoggerFactory {
    private JNILogger jniLogger;
    private File logFile = new File("./logs/latest.log");
    private String defaultName = this.logFile.getParent() + "/" + new VariableFactory("%date%.log.gz").date("yyyy-MM-dd");
    private String conflictName = this.logFile.getParent() + "/" + new VariableFactory("%date%-%i%.log.gz").date("yyyy-MM-dd");

    public LoggerFactory() {
        this.jniLogger = new JNILogger();

        if (this.logFile.exists())
            this.save();
    }

    public void setLogFile(String file) {
        this.logFile = new File(file);
    }

    public void setLogFile(File file) {
        this.logFile = file;
    }

    public String getDefaultName() {
        return this.defaultName;
    }

    public void setDefaultName(String name) {
        this.defaultName = name;
    }

    public String getConflictName() {
        return this.conflictName;
    }

    public void setConflictName(String name) {
        this.conflictName = name;
    }

    public Logger getLogger() {
        return new Logger(this);
    }

    public Logger getLogger(Class<?> clazz) {
        return new Logger(clazz, this);
    }

    public void log(String message) {
        message = ChatColor.stripColor(message);

        if (!this.logFile.getParentFile().exists())
            this.logFile.getParentFile().mkdirs();

        try {
            if (!this.logFile.exists())
                this.logFile.createNewFile();

            BufferedWriter writer = new BufferedWriter(new FileWriter(this.logFile.getPath(), true));
            writer.write(message + "\n");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        try {
            File file = new File(new VariableFactory(this.defaultName).date("yyyy-MM-dd").toString());
            for (int i = 1; file.exists(); i++)
                file = new File(new VariableFactory(this.conflictName).i(i).date("yyyy-MM-dd").toString());

            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(new FileOutputStream(file));
            FileInputStream fileInputStream = new FileInputStream(this.logFile);
            byte[] buf = new byte[1024];
            int len;
            while ((len = fileInputStream.read(buf)) > 0)
                gzipOutputStream.write(buf, 0, len);

            fileInputStream.close();
            gzipOutputStream.finish();
            gzipOutputStream.close();

            this.logFile.delete();
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