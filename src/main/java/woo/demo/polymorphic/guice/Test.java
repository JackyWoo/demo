package woo.demo.polymorphic.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;

/**
 * Created by wujianchao on 2020/1/19.
 */
public class Test {

    private static byte[] NULL_DATA = new byte[]{};

    @Inject
    Storage storage;

    public static void main(String[] args) {

        Injector ij = Guice.createInjector(new PropsModule(), new AbstractModule() {
            @Override
            protected void configure() {
                GuicePoly.registerImpl(binder(), "hdfs", HdfsStorage.class);
                GuicePoly.registerImpl(binder(), "s3", S3Storage.class);
                GuicePoly.registerImpl(binder(), "redis", RedisStorage.class);

                GuicePoly.bind(binder(), Storage.class, "guice.example.storage.type", "hdfs");
            }
        });

        ij.getInstance(Key.get(Test.class)).storage.push(NULL_DATA);
    }

}
