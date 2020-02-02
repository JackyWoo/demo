package woo.demo.guice;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Created by wujianchao on 2020/1/15.
 */
@Singleton
public class Person {

    public Person(){
        System.out.println("create Person");
    }

    @Inject
    private Car car;

    public void go(){
        car.run();
    }
}
