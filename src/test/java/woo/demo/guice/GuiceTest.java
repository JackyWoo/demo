package woo.demo.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Test;

/**
 * Created by wujianchao on 2020/1/15.
 */
public class GuiceTest {

    @Test
    public void test1(){
        Injector injector = Guice.createInjector();
        injector.getInstance(Person.class).go();
        injector.getInstance(Person.class).go();
    }
}
