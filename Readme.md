# Logger

> 轻量化的日志框架

版本: `0.0.1`

环境: `JAVA - 8`

操作系统: `Centos7及以上、Windows`



我在 window 10 环境下使用 log4j 这类框架时发现并不能正常替换字体颜色，在控制台中呈现的依旧是由 dos命令  `color`  设置的字体颜色，所以我想自己写一个日志框架。



已经支持日志等级

- `logger` -> 正常信息
- `debuf` -> 调试信息
- `warn` -> 警告信息
- `error` -> 错误信息



## 使用

我没有将这个项目上传maven仓库，所以如果想要用这个项目可能会麻烦一点。

1. 克隆仓库 [Logger]([hanhan2001/Logger: Java terminal logger (github.com)](https://github.com/hanhan2001/Logger))

2. 构建项目

   ```bash
   mvn clean package
   ```

3. 将打包出来的 jar 文件导入到本地 maven 仓库

   ```bash
   mvn install:install-file -DgroupId=me.xiaoying -DartifactId=Logger -Dversion=0.0.1 -Dpackaging=jar -Dfile=Logger.jar
   ```
   
   事实上可以直接使用 idea 提供的maven命令，可以省略掉步骤 2
   
4. 在 `pom.xml` 引入Logger

   ```xml
   <dependency>
       <groupId>me.xiaoying</groupId>
       <artifactId>Logger</artifactId>
       <version>0.0.1</version>
   </dependency>
   ```



## 示例代码



### 基本使用

```java
// 也可以这样创建logger -> Logger logger = LoggerFactory.getLogger(Main.class);
Logger logger = LoggerFactory.getLogger();
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



### 设置日志格式

> 你可以手动设置日志格式，这可能会导致日志其中一个原则: 准确性。
>
> 你可以监听 FormatChangeEvent 阻止别人修改日志

```java
import me.xiaoying.logger.Logger;
import me.xiaoying.logger.LoggerFactory;

Logger logger = LoggerFactory.getLogger();
/**
 * 默认支持的变量
 *
 * %message% -> 输出信息
 * %date% -> 当前时间
 * %class% -> 来源 class(需要用到另一个构造 Logger 方法LoggerFactory.getLogger(Main.class))
 * %level% -> 日志等级
 */
logger.setFormat("%message%");
```



### 彩色字体

> 我个人认为这个是最重要的，毕竟我在使用别的日志框架并不能设置彩色字体。
>
> 当然也有可能是我使用的方法有问题。

```java
import me.xiaoying.logger.Logger;
import me.xiaoying.logger.LoggerFactory;
import me.xiaoying.logger.ChatColor;

Logger logger = LoggerFactory.getLogger();
logger.info("&aGreen");
logger.info("{}Green", ChatColor.GREEN);
```



### 更改彩色关键字

```java
import me.xiaoying.logger.Logger;
import me.xiaoying.logger.LoggerFactory;
import me.xiaoying.logger.ChatColor;

Logger logger = LoggerFactory.getLogger();
logger.info(ChatColor.translateAlternateColorCodes('|', "|aGreen"));
logger.info(ChatColor.translateAlternateColorCodes('|', "|bBlie"));
logger.info(ChatColor.translateAlternateColorCodes('|', "|1Dark_blue"));
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



### 更改日志存储文件

> 你可以自定义日志产生的位置、文件名称，但更改之后可能会导致无法自动保存日志文件，我想这个问题我可能在以后的版本会修复

#### 未压缩文件

```java
import me.xiaoying.logger.LoggerFactory;

LoggerFactory.setLogFile("File");
LoggerFactory.setLogFile(new File("file"));
```



#### 压缩文件

```java
import me.xiaoying.logger.LoggerFactory;

/**
 * 这个只支持了 %date% 变量，你也可以在保存前手动处理文件的命名规则，处理完之后使用此方法即可
 */
LoggerFactory.setDefaultName("File");
/**
 * 当命名文件重复是时可以使用此方法
 * 此方法多了变量 %i%，这个变量代表当前文件是第几个重复文件，具体效果可以参考windows复制文件到同路径下时产生的文件名称
 */
LoggerFactory.getConflictName("File");
```



### 事件

> Logger 提供了简易的事件管理器，可以通过事件管理器进行很多操作。
>
> 比如事件 TerminalLogEndEvent 可以监听日志输出完毕的事件，利用这个可以实现输出完日志后输出符号 `>` 用来提醒用户此时此处可输入内容
>
> 我想以后可能会新建一个 Terminal 项目实现我说的方法



#### 编写监听器

```java
import me.xiaoying.logger.evet.Listener;
import me.xiaoying.logger.event.EventHandler;
import me.xiaoying.logger.event.terminal.TerminalLogEndEvent;

class MyListener implements Listener {
    @EventHandler
    public void onTerminalLogEnd(TerminalLogEndEvent event) {
        System.out.println("日志输出完毕");
    }
}
```



#### 注册事件

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
import me.xiaoying.logger.EventHandle;

EventHandle.callEvent(new MyEvent());
```



## Bug

我在使用的过程中发现如果颜色代码的 key 是特殊符号会导致颜色输出产生错误(例如符号: `§`)。

我想大概是因为 ASCII 无法处理特殊符号，有时间的话我会想办法修复这个问题。