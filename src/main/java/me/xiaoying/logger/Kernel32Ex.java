package me.xiaoying.logger;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinNT;

public interface Kernel32Ex extends Library {
    Kernel32Ex INSTANCE = Native.load("kernel32", Kernel32Ex.class);

    WinNT.HANDLE GetStdHandle(int nStdHandle);

    boolean SetConsoleTextAttribute(WinNT.HANDLE hConsoleOutput, int wAttributes);

    boolean SetConsoleOutputCP(int wCodePageID);

    int STD_OUTPUT_HANDLE = -11;
}