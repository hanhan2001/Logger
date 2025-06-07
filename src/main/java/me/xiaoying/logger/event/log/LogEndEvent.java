package me.xiaoying.logger.event.log;

/**
 * 当打印完成将触发此事件
 */
public class LogEndEvent extends LogEvent {
    public LogEndEvent(String message) {
        super(message);
    }
}