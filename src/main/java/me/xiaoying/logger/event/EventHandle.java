package me.xiaoying.logger.event;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class EventHandle {
    private final static Set<RegisteredListener> registeredListeners = new HashSet<>();

    /**
     * 注册事件
     *
     * @param listener Listener
     */
    public static void registerEvent(Listener listener) {
        List<Method> methods = new ArrayList<>();
        for (Method method : listener.getClass().getMethods()) {
            if (method.getAnnotation(EventHandler.class) == null)
                continue;

            if (method.getParameters().length != 1)
                return;

            Class<?> father = method.getParameters()[0].getType().getSuperclass();
            while (father.getSuperclass() != Object.class)
                father = father.getSuperclass();

            if (father != Event.class)
                return;

            methods.add(method);
        }

        if (methods.isEmpty())
            return;

        EventHandle.registeredListeners.add(new RegisteredListener(listener, methods));
    }

    /**
     * 取消注册 事件
     *
     * @param listener Listener
     */
    public static void unregisterEvent(Listener listener) {
        Iterator<RegisteredListener> registeredListenerIterator = EventHandle.registeredListeners.iterator();

        RegisteredListener registeredListener;
        while ((registeredListener = registeredListenerIterator.next()) != null) {
            if (registeredListener.getListener() != listener)
                continue;

            EventHandle.registeredListeners.remove(registeredListener);
        }
    }

    /**
     * 触发事件
     *
     * @param event Event
     */
    public static void callEvent(Event event) {
        EventHandle.registeredListeners.forEach(registeredListener -> registeredListener.getMethods().forEach(method -> {
            Parameter[] parameters = method.getParameters();

            if (parameters.length != 1)
                return;

            if (parameters[0].getType() != event.getClass())
                return;

            try {
                Constructor<?> constructor = registeredListener.getListener().getClass().getConstructor();
                Object instance = constructor.newInstance();

                Method m = instance.getClass().getMethod(method.getName(), event.getClass());
                m.invoke(instance, event);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }
}