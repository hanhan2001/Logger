package me.xiaoying.logger.event.log;

import me.xiaoying.logger.event.Cancelable;

/**
 * 准备打印日志时将触发此事件
 */
public class PrepareLogEvent extends LogEvent implements Cancelable {
    private boolean cancelled = false;

    private String message;

    public PrepareLogEvent(String message) {
        super(message);

        this.message = message;
    }

    /**
     * 获取打印内容
     *
     * @return 将要打印的内容
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * 设置打印内容
     *
     * @param message 新的打印内容
     */
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean isCanceled() {
        return this.cancelled;
    }

    @Override
    public void setCanceled(boolean canceled) {
        this.cancelled = canceled;
    }
}