package top.huzhurong.test.other;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.util.Modules;
import top.huzhurong.test.bootcore.Agent;
import top.huzhurong.test.bootcore.AgentOption;
import top.huzhurong.test.common.log.PLoggerBinder;
import top.huzhurong.test.common.log.PLoggerFactory;
import top.huzhurong.test.other.log.Slf4jLoggerBinder;
import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import top.huzhurong.test.other.module.AppModule;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

/**
 * @author chenshun00@gmail.com
 * @since 2019/2/15
 */
public class DefaultAgent implements Agent {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private AgentOption agentOption;
    private PLoggerBinder pLoggerBinder;


    public DefaultAgent(AgentOption agentOption) {
        this.agentOption = agentOption;
        Thread thread = Thread.currentThread();
        ClassLoader contextClassLoader = thread.getContextClassLoader();
        try {
            thread.setContextClassLoader(this.getClass().getClassLoader());
            String path = System.getProperty("log4j") + "/log4j.xml";
            DOMConfigurator.configure(path);
        } finally {
            thread.setContextClassLoader(contextClassLoader);
        }
    }

    @Override
    public boolean start() {
        pLoggerBinder = new Slf4jLoggerBinder();
        PLoggerFactory.initialize(pLoggerBinder);
        logger.info("成功加载DefaultAgent :{}", logger.getClass().getClassLoader());
        Module module = module(agentOption);
        Injector injector = Guice.createInjector(Stage.PRODUCTION, module);
        Instrumentation instrumentation = injector.getInstance(Instrumentation.class);
        logger.info("Instrumentation:[{}] [{}] [{}]", instrumentation, instrumentation.getClass().getClassLoader(), this.getClass().getClassLoader());
        ClassFileTransformer classFileTransformer = injector.getInstance(ClassFileTransformer.class);
        logger.info("[ClassFileTransformer]:[{}]", classFileTransformer);
        instrumentation.addTransformer(classFileTransformer);
        return false;
    }

    private Module module(AgentOption agentOption) {
        AppModule appmodule = new AppModule(agentOption);
        return Modules.combine(appmodule);
    }
}
