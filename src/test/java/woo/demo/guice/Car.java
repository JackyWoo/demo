package woo.demo.guice;

import com.google.inject.Inject;

/**
 * Created by wujianchao on 2020/1/15.
 */
public class Car {

    public Car(){
        System.out.println("create Car");
    }

    public void run(){
        System.out.println("car runing");
    }
}
