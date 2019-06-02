* 使用

```bash
 cd agent
 mvn clean install -Dmaven.test.skip=true
 # 类unix
 cp boot-agent/target/boot-agent-1.0-SNAPSHOT.zip ~/Desktop
 # windows
 # move /y boot-agent/target/boot-agent-1.0-SNAPSHOT.zip
```

1、安装Elasticsearch 5.3.0
2、启动 `boot-collect#CollectApplication`
3、添加 `-javaagent:~/xxx/xxx.jar` 置 `jvm参数`
4、启动项目，打开kibana即可查看数据

#### Asm 和 agent 的使用

*   注意点

jvm 中，一份class文件是一致的 `(equals(class) = true)` 的前提是类是一致的，加载该类的 `classLoader` 是一样的才是相同的class
如果class是一样的，但是 `classLoader` 不一致，那么equals方法会返回false。表明不是同一个类，jdk本身自带的因为都是`boot` 加载的，所以没有问题

*   使用场景
    *   监控(APM)，例如 `rt`,`错误率`,`慢sql`,`sdk 调用率` , `memcache/redis qps`等等
    *   安全审计

> 相比aop的优势，应用代码无侵入，取消 `-javaagent` 参数即可取消监控

#### Todo

*   代码优化
*   前端UI正在规划当中

#### 参考资料

[https://github.com/alibaba/TProfiler](https://github.com/alibaba/TProfiler)

[https://github.com/naver/pinpoint](https://github.com/naver/pinpoint)

[https://github.com/btraceio/btrace](https://github.com/btraceio/btrace)