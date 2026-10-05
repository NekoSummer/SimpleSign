# SimpleSign

简易且硬核的签到插件

配置文件 (src/main/java/cn/xinyue_neko/simpleSign/config/Config.java)：
```java
package cn.xinyue_neko.plugin.simpleSign.config;

public interface Config {
    /** 设置签到数额 */
    double SIGN_MONEYS = 1000.00;

    /** 年月日 */
    String DATE_TIME_FORMAT = "yyyy-MM-dd"; 
    
    /** 签到消息 */
    String MESSAGES_SUCCESS = "&b签到成功, 你已获得 {money} 元钱";
    String MESSAGE_ALREADY = "&7你今天已经签到过了，明天再来吧.";
    String MESSAGE_ERROR = "发生错误: {error}";

}
```

改完请使用 ``` mvn clean package ``` 或 ``` ./gradlew clean build ``` 编译后将jar放进服务的plugins目录里