package woo.demo.guice.override;

import com.google.common.collect.ImmutableList;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import woo.demo.guice.obj.storage.Storage;

import java.util.List;

/**
 * Created by wujianchao on 2020/2/2.
 */
public class TestMain {

    public static void main(String[] args) {


        List<Module> l1 = ImmutableList.of(new HdfsModule());
        List<Module> l2 = ImmutableList.of(new S3Module());

        Module m = Modules.override(l1).with(l2);
        System.out.println(m);

        Injector ij = Guice.createInjector(m);
        Storage s = ij.getInstance(Storage.class);


        s.push(null);

    }
}
