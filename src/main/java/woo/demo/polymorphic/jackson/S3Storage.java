package woo.demo.polymorphic.jackson;

/**
 * Created by wujianchao on 2020/1/19.
 */
public class S3Storage implements Storage {

    private String path;

    private String bucket;

    @Override
    public void push(byte[] data) {
        System.out.println("s3 storage push some data");
    }
}
