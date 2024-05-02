# Logger

> Hmmmm, I want use code to change log format when i developing some project, but I don't know how to change Log4j's log format.
>
> So, I decide to make new logger by myself



## Use

Hmmmm, If you want use this logger, that is a little complex.

1. clone this repository

2. build this project
3. use maven command `mvn install:install-file -DgroupId=me.xiaoying -DartifactId=Logger -Dversion=0.0.1 -Dpackaing=jar -Dfile=Logger-V0.0.1.jar`
4. import Logger to `pom.xml`

```xml
<dependency>
    <groupId>me.xiaoying</groupId>
    <artifactId>Logger</artifactId>
    <version>0.0.1</version>
</dependency>
```

If want use this logger in gradle, you can set `mavenLocal()`, but I tried this way, it can't work.



## Example

- [Example Code](https://github.com/hanhan2001/Logger/Example.md)



## Bug

If you use `ChatColor.translateAlternateColorCodes`, you need to be careful `altCharColor` can't use some special sign like `§`.

I will fix it when I'm free.



## Description

When I used Log4j in the Windows 10, terminal can't set text color.

But Minecraft server used Log4j can use it, so I tried many ways to implement text color in Windows 10 terminal, but none of them worked.

So I make a new logger.