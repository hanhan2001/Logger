package me.xiaoying.logger.printstream;

import me.xiaoying.logger.Logger;

import java.io.OutputStream;
import java.io.PrintStream;

public class LErrorPrintStream extends PrintStream {
    Logger logger = new Logger();

    public LErrorPrintStream(OutputStream out) {
        super(out);
        this.logger.setFormat("%message%");
    }

    @Override
    public void println() {
        this.println(" ");
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
    public void println(String x) {
        this.logger.info(x);
    }

    @Override
    public void println(Object x) {
        this.println(String.valueOf(x));
    }
}