# Logger

> 轻量化日志框架
>
> 比起 log4j2、slf4j 这类日志框架，这个仓库并没有太大的优势。
>
> 写这个一方面是为了满足我刚开始接触编程对控制台彩色文字原理(非 ANSI 编码转义)的好奇；
>
> 另一方面是因为我之前在 win 10 的环境下使用 log4j 和 slf4j 发现无法正常替换颜色，所以写了一个这个。

版本: `1.0.0`

环境: `JAVA - 8`

重构所有代码，整体更简洁轻量。

移除了日志文件的配置，我觉得既然支持 Event，那么对于日志的存储应该交给服务自己处理，所以我移除了这个功能。



已经支持日志等级

- `logger` -> 正常信息
- `debuf` -> 调试信息
- `warn` -> 警告信息
- `error` -> 错误信息

原本是想支持自定义日志等级的，但我觉得我可能哪天就把 Logger 删了，所以就没做额外的功能了



## ⚙️配置依赖

> 我没有将 Logger 上传至 mvnrepository，所以需要手动安装到本地

### 安装到本地

Maven

```bash
mvn install:install-file -DgroupId=me.xiaoying -DartifactId=logger -Dversion={版本} -Dpackaging=jar -Dfile={从 release 中下载的 jar 所在路径}
```

gradle

```bash
./gradlew publishToMavenLocal
```

需要注意的是使用 gradle 需要先编译出 jar 包才会导入到本地仓库。如果你没有安装 Logger 使用的 gradle wrapper 版本(8.9)，则会先下载 gradle(也可以自行修改使用版本)，并且会下载 Logger 使用的所有依赖

### 项目中引用

#### Maven

```xml
<dependency>
    <groupId>me.xiaoying</groupId>
    <artifactId>logger</artifactId>
    <version>{$version}</version>
</dependency>
```

#### Gradle

```kotlin
implementation("me.xiaoying:logger:$version")
```



## 🧭基础示例

### 基本使用

```java
Logger logger = LoggerFactory.getLogger();
// 添加 class 能够更好的统一管理同 class 的 logger 对象，如果没有 class 则每次会创建一个新的 Logger

logger.info("Hello World");
logger.debug("Hello World");
logger.warn("Hello World");
logger.error("Bed World");

String author = "筱樱";
logger.info("Author: {}", author);

int version = 1;
String project = "Logger";
logger.info("Name - {}, Version - {}", project, version);
```



### 日志格式

> log4j2、slf4j 这类日志框架，或者说很多成熟的框架都喜欢专门写一个配置文件来配置项目。我不太清楚为什么要这样做，这样反倒难以贴合我具体的开发目标。

```java
import me.xiaoying.logger.Logger;
import me.xiaoying.logger.LoggerFactory;

public class Test {
    public static void main(String[] args) {
        // 获取 Test.class 的 Logger，这样可以在别处通过同样的操作获取到 Test 的同一个 Logger
		Logger logger = LoggerFactory.getLogger(Test.class);
        // 获取 logger 当前的格式
        // Logger#getFormat 还可以传递一个 Logger.Level 的参数
        // 当不存在 Logger.Level 的参数则获取到的是统一(所有日志等级)的格式
        // 反之如果设置了对应 Logger.Level 的日志格式则会返回对应的日志格式，没有设置则返回统一的日志格式
        String originFormat = logger.getFormat();
        logger.setFormat("%message%");
        logger.info("[>] 请输入账号");
        logger.setFormat(originFormat);
    }
}
```



### 彩色字体

> 我个人认为这个是最重要的，毕竟我在使用别的日志框架并不能设置彩色字体。
>
> 当然也有可能是我使用的方法有问题。

```java
import me.xiaoying.logger.Logger;
import me.xiaoying.logger.LoggerFactory;
import me.xiaoying.logger.ChatColor;

public class Test {
    public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger();
		logger.info("&aGreen");
		logger.info("{}Green", ChatColor.GREEN);
    }
}
```



### 更改彩色关键字

> Logger 底层的颜色代码判断依据符号是 §
>
> 更改颜色关键字的功能在 Logger 内部就有实现(将 `&` 替换成 `§`)，其实有点多此一举了，不过没人用也就不管了。

```java
import me.xiaoying.logger.Logger;
import me.xiaoying.logger.LoggerFactory;
import me.xiaoying.logger.ChatColor;

public class Test {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger();
		logger.info(ChatColor.translateAlternateColorCodes('|', "|aGreen"));
		logger.info(ChatColor.translateAlternateColorCodes('|', "|bBlie"));
		logger.info(ChatColor.translateAlternateColorCodes('|', "|1Dark_blue"));
    }
}
```

你也可以新建一个方法或者类来替换掉长串的代码

```java
import me.xiaoying.logger.Logger;
import me.xiaoying.logger.LoggerFactory;
import me.xiaoying.logger.ChatColor;

public class Test {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger();
        logger.info(ColorUtil.translate("|bHello World"));
    }
    
    public class ColorUtil {
        public static String translate(String text) {
            return ChatColor.translateAlternateColorCodes('|', text);
        }
    }
}
```



### 事件

> Logger 提供了简易的事件管理器，可以通过事件管理器进行很多操作。
>
> 比如事件 LogEndEvent 可以监听日志输出完毕的事件，利用这个可以实现输出完日志后输出符号 `>` 用来提醒用户此时此处可输入内容
>



#### 编写监听器

```java
import me.xiaoying.logger.event.Listener;
import me.xiaoying.logger.event.EventHandler;
import me.xiaoying.logger.event.terminal.TerminalLogEndEvent;

class MyListener implements Listener {
    @EventHandler
    public void onPrepareLog(PrepareLogEvent event) {
        // 打印一个置顶符号以实现日志信息替换掉已经打印出来的 > 符号
        System.out.print("\r");
    }
    
    @EventHandler
    public void onLogEnd(LogEndEvent event) {
        // 打印出 > 表示此处可以输入
        System.out.print("> ");
    }
}
```



#### 注册监听器

```java
EventHander.registerEvent(new MyListener());
```



#### 自定义事件

```java
import me.xiaoying.logger.event.Event;

class MyEvent extends Event {
    
}
```



#### 触发事件

```java
EventHandle.callEvent(new MyEvent());
```
