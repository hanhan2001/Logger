package me.xiaoying.logger.event;

import java.lang.reflect.Method;
import java.util.List;

public class RegisteredListener {
    private final Listener listener;
    private final List<Method> methods;

    public RegisteredListener(Listener listener, List<Method> methods) {
        this.listener = listener;
        this.methods = methods;
    }

    public Listener getListener() {
        return this.listener;
    }

    public List<Method> getMethods() {
        return this.methods;
    }
}