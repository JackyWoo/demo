package woo.demo.polymorphic.guice;


/**
 * Created by wujianchao on 2020/1/19.
 */
public class RedisStorage implements Storage {

    @Override
    public void push(byte[] data) {
        System.out.println("Redis push some data");
    }

}
