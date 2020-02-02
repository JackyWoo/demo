package woo.demo.guice.dependency;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Created by wujianchao on 2020/1/20.
 */
public class Test {

    public static void main(String[] args) {
        Injector parent = Guice.createInjector(new CarModule());
        Injector child = parent.createChildInjector(new PersonModule());

        parent.getInstance(Person.class).go();
    }
}
