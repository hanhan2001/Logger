package me.xiaoying.logger;

public class Test {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Test.class);
        logger.info("Hello Worl阿萨大d", "");
        logger.setDateFormat("HH:mm:ss");
        logger.info("&e你好，&d世界&8fff", "");
    }
}