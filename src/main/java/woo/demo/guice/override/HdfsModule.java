package woo.demo.guice.override;

import com.google.inject.AbstractModule;
import woo.demo.guice.obj.storage.HdfsStorage;
import woo.demo.guice.obj.storage.Storage;

/**
 * Created by wujianchao on 2020/2/2.
 */
public class HdfsModule extends AbstractModule {

    @Override
    protected void configure() {
        binder().bind(Storage.class).to(HdfsStorage.class);
    }
}
