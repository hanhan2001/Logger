package me.xiaoying.logger;

import java.io.*;

public class JNILogger {
    private int VERSION = 1;

    public native void send(String message, String altCharColor);

    public JNILogger() {
        File file = new File(System.getProperty("user.home") + "/AppData/Roaming/XiaoYing/JNILogger_" + VERSION + ".dll");
        if (!file.exists())
            this.saveResource(file.getName(), file.getParent());

        System.setProperty("java.library.path", file.getPath());
        System.load(file.getAbsolutePath());
    }

    private void saveResource(String filename, String path) {
        File file = new File(path + "/" + filename);

        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();

        InputStream inputStream = JNILogger.class.getClassLoader().getResourceAsStream(filename);
        try {
            OutputStream outputStream = new FileOutputStream(file.getPath());
            int byteCount = 0;
            byte[] bytes = new byte[1024];
            while ((byteCount = inputStream.read(bytes)) != -1)
                outputStream.write(bytes, 0, byteCount);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}