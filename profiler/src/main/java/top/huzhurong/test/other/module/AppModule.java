package top.huzhurong.test.other.module;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import top.huzhurong.test.bootcore.AgentOption;
import top.huzhurong.test.bootcore.template.TranTemplate;
import top.huzhurong.test.common.plugin.PluginLoader;
import top.huzhurong.test.other.annotation.PluginJars;
import top.huzhurong.test.other.module.provider.PluginLoaderProvider;
import top.huzhurong.test.other.transform.DefaultTransformer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.util.List;

/**
 * @author chenshun00@gmail.com
 * @since 2019/2/15
 */
public class AppModule extends AbstractModule {

    private AgentOption agentOption;

    public AppModule(AgentOption agentOption) {
        this.agentOption = agentOption;
    }

    @Override
    protected void configure() {
        binder().requireAtInjectOnConstructors();
        binder().requireExplicitBindings();

        bind(Instrumentation.class).toInstance(agentOption.getInstrumentation());
        bind(ClassFileTransformer.class).to(DefaultTransformer.class);
        bind(ClassLoader.class).toInstance(agentOption.getClassLoader());

        TypeLiteral<List<String>> pluginJarFile = new TypeLiteral<List<String>>() {
        };
        bind(pluginJarFile).annotatedWith(PluginJars.class).toInstance(agentOption.getPluginJars());

        bind(PluginLoader.class).toProvider(PluginLoaderProvider.class).in(Scopes.SINGLETON);

        bind(AgentOption.class).toInstance(agentOption);

        TranTemplate tranTemplate = new TranTemplate();
        bind(TranTemplate.class).toInstance(tranTemplate);
    }
}
