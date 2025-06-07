package me.xiaoying.logger.render;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;

import java.util.Arrays;
import java.util.List;

interface Kernel32Ex extends Library {
    int STD_OUTPUT_HANDLE = -11;
    Kernel32Ex INSTANCE = Native.load("kernel32", Kernel32Ex.class);

    WinNT.HANDLE GetStdHandle(int nStdHandle);

    boolean SetConsoleTextAttribute(WinNT.HANDLE hConsoleOutput, int wAttributes);

    boolean SetConsoleOutputCP(int wCodePageID);

    /**
     * 控制台中输出文字
     *
     * @param hConsoleOutput 句柄
     * @param lpBuffer char buffer
     * @param nNumberOfCharsToWrite 字符串长度
     * @param lpNumberOfCharsWritten 字符串指针传递
     * @param lpReserved 指针
     * @return 是否正常执行
     */
    boolean WriteConsoleW(WinNT.HANDLE hConsoleOutput, char[] lpBuffer,
                          int nNumberOfCharsToWrite,
                          IntByReference lpNumberOfCharsWritten,
                          Pointer lpReserved);

    // 获取控制台屏幕缓冲区信息
    boolean GetConsoleScreenBufferInfo(WinNT.HANDLE hConsoleOutput, CONSOLE_SCREEN_BUFFER_INFO lpConsoleScreenBufferInfo);

    class CONSOLE_SCREEN_BUFFER_INFO extends Structure {
        public COORD dwSize;
        public COORD dwCursorPosition;
        public short wAttributes;
        public SMALL_RECT srWindow;
        public COORD dwMaximumWindowSize;

        protected List<String> getFieldOrder() {
            return Arrays.asList("dwSize", "dwCursorPosition", "wAttributes", "srWindow", "dwMaximumWindowSize");
        }
    }

    class COORD extends Structure {
        public short X;
        public short Y;

        protected List<String> getFieldOrder() {
            return Arrays.asList("X", "Y");
        }
    }

    class SMALL_RECT extends Structure {
        public short Left;
        public short Top;
        public short Right;
        public short Bottom;

        protected List<String> getFieldOrder() {
            return Arrays.asList("Left", "Top", "Right", "Bottom");
        }
    }
}