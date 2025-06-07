package me.xiaoying.logger.event;

public interface Cancelable {
    boolean isCanceled();

    void setCanceled(boolean canceled);
}
