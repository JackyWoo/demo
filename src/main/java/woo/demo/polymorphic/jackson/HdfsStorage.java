package woo.demo.polymorphic.jackson;

import com.google.inject.Inject;

/**
 * Created by wujianchao on 2020/1/19.
 */
public class HdfsStorage implements Storage {

    private String path;

    @Override
    public void push(byte[] data) {
        System.out.println("hdfs storage push some data");
    }
}
