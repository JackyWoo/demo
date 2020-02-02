package woo.demo.guice.dependency;

import com.google.inject.Inject;

/**
 * Created by wujianchao on 2020/1/20.
 */
public class Person {

    @Inject
    private Car car;

    public void go(){
        System.out.println("person go");
        car.run();
    }
}
