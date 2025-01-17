package me.xiaoying.logger.printstream;

import me.xiaoying.logger.Logger;

import java.io.OutputStream;
import java.io.PrintStream;

public class LOutPrintStream extends PrintStream implements LPrintStream {
    private Logger logger;

    public LOutPrintStream(OutputStream out) {
        super(out);
        this.logger = new Logger();
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void println() {
        this.println(" ");
    }

    @Override
    public void println(Object x) {
        if (x == null) {
            this.println("null");
            return;
        }
        this.println(x.toString());
    }

    @Override
    public void println(String x) {
        if (x == null) {
            this.logger.info("null");
            return;
        }
        this.logger.info(x);
    }

    /**
     * Emm, that can't print message one line
     *
     * @param x  an array of chars to print.
     */
    @Override
    public void println(char[] x) {
        for (char c : x)
            this.println(c);
    }

    @Override
    public void println(double x) {
        this.println(String.valueOf(x));
    }

    @Override
    public void println(float x) {
        this.println(String.valueOf(x));
    }

    @Override
    public void println(long x) {
        this.println(String.valueOf(x));
    }

    @Override
    public void println(int x) {
        this.println(String.valueOf(x));
    }

    @Override
    public void println(char x) {
        this.println(String.valueOf(x));
    }

    @Override
    public void println(boolean x) {
        this.println(String.valueOf(x));
    }

    @Override
    public Logger getLogger() {
        return this.logger;
    }
}