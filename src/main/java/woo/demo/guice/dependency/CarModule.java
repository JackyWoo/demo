package woo.demo.guice.dependency;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * Created by wujianchao on 2020/1/20.
 */
public class CarModule extends AbstractModule {

    @Override
    protected void configure() {
        binder().bind(Car.class).in(Scopes.SINGLETON);
    }
}
