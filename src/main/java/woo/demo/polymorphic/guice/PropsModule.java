package woo.demo.polymorphic.guice;

import com.google.common.base.Throwables;
import com.google.inject.AbstractModule;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by wujianchao on 2020/1/19.
 */
public class PropsModule extends AbstractModule {

    @Override
    protected void configure() {
        Properties props = new Properties();
        try {
            props.load(ClassLoader.getSystemResourceAsStream("guice-example.properties"));
        } catch (IOException e) {
            Throwables.propagate(e);
        }
        binder().bind(Properties.class).toInstance(props);
    }
}
