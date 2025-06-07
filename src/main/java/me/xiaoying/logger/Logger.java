package me.xiaoying.logger;


import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;

public class Logger {
    private Class<?> clazz;

    protected Logger() {

    }

    protected Logger(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void info(String message) {
        Kernel32Ex kernel32 = Kernel32Ex.INSTANCE;
        kernel32.SetConsoleTextAttribute(
                Kernel32.INSTANCE.GetStdHandle(Kernel32.STD_OUTPUT_HANDLE),
                0x0C);
        System.out.println(message);
        WinNT.HANDLE hConsole = kernel32.GetStdHandle(Kernel32Ex.STD_OUTPUT_HANDLE);
        kernel32.SetConsoleTextAttribute(hConsole, 0x07);
        System.out.println("恢复正常颜色");
    }

    public void info(String format, String... args) {

    }

    public void logger(String message) {

    }

    enum Level {
        INFO('a'),
        WARN('e'),
        ERROR('c'),
        DEBUG('b');

        private final char color;

        Level(char color) {
            this.color = color;
        }

        public char getColor() {
            return this.color;
        }
    }
}