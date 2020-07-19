package woo.demo.polymorphic.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;
import woo.demo.guice.obj.animal.Animal;
import woo.demo.guice.obj.animal.Cat;
import woo.demo.guice.obj.animal.Dog;
import woo.demo.guice.obj.storage.HdfsStorage;
import woo.demo.guice.obj.storage.RedisStorage;
import woo.demo.guice.obj.storage.S3Storage;
import woo.demo.guice.obj.storage.Storage;

import java.util.Properties;

/**
 * Created by wujianchao on 2020/1/19.
 */
public class TestMain {


    public static void main(String[] args) {

        Injector ij = Guice.createInjector(new PropsModule(), new AbstractModule() {
            @Override
            protected void configure() {
                GuicePoly.registerImpl(binder(), Storage.class, "hdfs", HdfsStorage.class);
                GuicePoly.registerImpl(binder(), Storage.class, "s3", S3Storage.class);
                GuicePoly.registerImpl(binder(), Storage.class, "redis", RedisStorage.class);

                GuicePoly.bind(binder(), Storage.class, "guice.example.storage.type", "hdfs");
            }
        });

        ij.getInstance(Storage.class).push(null);


        Properties props = new Properties();
        props.put("animal.type", "cat");

        Injector ij2 = Guice.createInjector(
                (binder) -> binder.bind(Properties.class).toInstance(props),
                (binder) -> {
                    GuicePoly.registerImpl(binder, Animal.class, "cat", Cat.class);
                    GuicePoly.registerImpl(binder, Animal.class, "dog", Dog.class);
                    GuicePoly.bind(binder, Animal.class, "animal.type", "cat");
                }
        );

        System.out.println(ij2.getInstance(Dog.class).say());

        Injector ij3 = Guice.createInjector(
                (binder) -> binder.bind(Properties.class).toInstance(props),
                Modules.override(new CatModule()).with(new DogModule()),
                (binder) -> {
                    GuicePoly.bind(binder, Animal.class, "animal.type", "cat");
                }
        );
        System.out.println(ij2.getInstance(Dog.class).say());
    }

}
