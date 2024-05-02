# Example

That is esay, you just need to use only one line code to constracutor Logger.

```java
Logger logger = LoggerFactory.getLogger();
```

If you want add class name to log info, you can do like this.

```java
Logger logger = LoggerFactory().getLogger(YourClass.class);
```

And use it.

```java
Logger logger = LoggerFactory.getLogger();
logger.info("Hello {}", "World");
```



## File

Hmmmm, this file is use to record info for Terminal.

You can change file path, like this:

```java
LoggerFactory.setLogFile(New File("Path"));
```



## Format

You can change log format in code if you want.

```java
Logger logger = LoggerFactory.getLogger();
logger.setFormat("%date% [%level%] - %message%");
```



## Level

You can use this logger's level like Log4j's

```java
logger.info();
logger.warn();
logger.error();
```



## Color

If you want set color for text, you can do this.

```java
Logger logger = LoggerFactory.getLogger();
logger.info("&aGreen");
logger.info("&cRed");
logger.info("&1dark_blue");
```

You can use `ChatColor.translateAlternateColorCodes()` to change keyword for text color.

```java
Logger logger = LoggerFactory.getLogger();
logger.info(ChatColor.translateAlternateColoeCodes('|', "|aGreen"));
logger.info(ChatColor.translateAlternateColoeCodes('|', "|cRed"));
logger.info(ChatColor.translateAlternateColoeCodes('|', "|1Dark_blue"));
```

And you can make a new class to instead this code.

```java
public class Test {
    public static void Main(String[] args) {
        Logger logger = LoggerFactory.getLogger();
        logger.info(ColorUtil.translate("|aGreen"));
        logger.info(ColorUtil.translate("|cRed"));
        logger.info(ColorUtil.translate("|1Dark_blue"));
    }
    
	public class ColorUtil {
    	public static String translate(String text) {
            return ChatColor.translateAlternateColorCodes('|', text);
        }
	}
}
```