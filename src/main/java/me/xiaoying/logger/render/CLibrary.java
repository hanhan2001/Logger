package me.xiaoying.logger.render;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface CLibrary extends Library {
    CLibrary INSTANCE = Native.load("c", CLibrary.class);

    int write(int fd, byte[] buf, int count);
}