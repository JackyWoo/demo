package woo.demo.polymorphic.jackson;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by wujianchao on 2020/1/19.
 */
@JsonTypeName("redis")  // register implementation
@JsonIgnoreProperties(ignoreUnknown = true)
public class RedisStorage implements Storage {

    @Override
    public void push(byte[] data) {
        System.out.println("Redis push some data");
    }

}
