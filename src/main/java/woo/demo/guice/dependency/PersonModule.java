package woo.demo.guice.dependency;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * Created by wujianchao on 2020/1/20.
 */
public class PersonModule extends AbstractModule {

    @Override
    protected void configure() {
        binder().bind(Person.class).in(Scopes.SINGLETON);
    }
}
