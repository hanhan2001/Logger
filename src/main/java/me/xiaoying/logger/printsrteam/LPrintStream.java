package me.xiaoying.logger.printsrteam;

import me.xiaoying.logger.Logger;
import me.xiaoying.logger.LoggerFactory;
import me.xiaoying.logger.utils.ColorUtil;

import java.io.OutputStream;
import java.io.PrintStream;

public class LPrintStream extends PrintStream {
    private final Logger.Level level;

    public LPrintStream(OutputStream out) {
        super(out);

        if (out == System.out)
            this.level = Logger.Level.INFO;
        else if (out == System.err)
            this.level = Logger.Level.ERROR;
        else
            this.level = Logger.Level.DEBUG;
    }

    @Override
    public void print(boolean b) {
        this.print(String.valueOf(b));
    }

    @Override
    public void print(char c) {
        this.print(String.valueOf(c));
    }

    @Override
    public void print(int i) {
        this.print(String.valueOf(i));
    }

    @Override
    public void print(long l) {
        this.print(String.valueOf(l));
    }

    @Override
    public void print(float f) {
        this.print(String.valueOf(f));
    }

    @Override
    public void print(double d) {
        this.print(String.valueOf(d));
    }

    @Override
    public void print(char[] s) {
        this.print(String.valueOf(s));
    }

    @Override
    public void print(String s) {
        this.println(s);
    }

    @Override
    public void print(Object obj) {
        this.print(String.valueOf(obj));
    }

    @Override
    public void println() {
        this.println();
    }

    @Override
    public void println(boolean x) {
        this.println(String.valueOf(x));
    }

    @Override
    public void println(char x) {
        this.println(String.valueOf(x));
    }

    @Override
    public void println(int x) {
        this.println(String.valueOf(x));
    }

    @Override
    public void println(long x) {
        this.println(String.valueOf(x));
    }

    @Override
    public void println(float x) {
        this.println(String.valueOf(x));
    }

    @Override
    public void println(double x) {
        this.println(String.valueOf(x));
    }

    @Override
    public void println(char[] x) {
        this.println(String.valueOf(x));
    }

    @Override
    public void println(Object x) {
        this.println(String.valueOf(x));
    }

//    @Override
//    public PrintStream printf(String format, Object... args) {
//        return this.printf(format, args);
//    }
//
//    @Override
//    public PrintStream printf(Locale l, String format, Object... args) {
//        return this.printf(l, format, args);
//    }
    
    @Override
    public void println(String string) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        String className = null;
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            if (stackTraceElement.getClassName().startsWith("me.xiaoying.logger."))
                continue;

            className = stackTraceElement.getClassName();
        }

        try {
            Logger logger = LoggerFactory.getLogger(LPrintStream.class.getClassLoader().loadClass(className));

            switch (this.level) {
                case INFO:
                    logger.info(string);
                    break;
                case ERROR: {
                    if (!this.isStackTraceLine(string)) {
                        logger.error(string);
                        break;
                    }

                    String origin = logger.getFormat();

                    logger.setFormat(ColorUtil.translate("&c%message%"));
                    logger.warn(string);

                    logger.setFormat(origin);
                    break;
                }
                case DEBUG:
                    logger.debug(string);
                    break;
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isStackTraceLine(String line) {
        return line.startsWith("\tat ") ||
                line.startsWith("Caused by: ") ||
                line.startsWith("Suppressed: ") ||
                line.matches("^[\\w.]*Exception.*") ||
                line.startsWith("Exception in thread ") ||
                line.trim().startsWith("...") ||
                line.trim().matches("\\.\\.\\. \\d+ more");
    }
}