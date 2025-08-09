package me.xiaoying.logger;

import me.xiaoying.logger.utils.ColorUtil;
import me.xiaoying.logger.utils.DateUtil;

class VariableFactory {
    private String string;

    public VariableFactory(String string) {
        this.string = string;
    }

    public VariableFactory clazz(Class<?> clazz) {
        if (clazz == null)
            return this;

        this.string = this.string.replace("%class%", clazz.getName());
        return this;
    }

    public VariableFactory date(String dateFormat) {
        this.string = this.string.replace("%date%", DateUtil.getDate(dateFormat));
        return this;
    }

    public VariableFactory level(Logger.Level level) {
        this.string = this.string.replace("%level%", level.toString());
        return this;
    }

    public VariableFactory message(String message) {
        this.string = this.string.replace("%message%", message);
        return this;
    }

    public VariableFactory parameters(Object... args) {
        if (!this.string.contains("{}"))
            return this;

        int index = 0;

        while (this.string.contains("{}")) {
            if (index > args.length - 1)
                break;

            this.string = this.string.replaceFirst("\\{}", String.valueOf(args[index]));
        }

        return this;
    }

    public VariableFactory color() {
        this.string = ColorUtil.translate(this.string);
        return this;
    }

    @Override
    public String toString() {
        return this.string;
    }
}