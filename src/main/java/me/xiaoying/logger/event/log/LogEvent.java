package me.xiaoying.logger.event.log;

import me.xiaoying.logger.event.Event;

public abstract class LogEvent extends Event {
    private String message;

    public LogEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}