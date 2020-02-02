package woo.demo.polymorphic.guice;

import com.google.inject.*;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.util.Types;

import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.Properties;

/**
 * Created by wujianchao on 2020/1/19.
 */
public class GuicePoly<T> {

    public static <T> void registerImpl(Binder binder, String implName, Class<T> implClazz){
        MapBinder.newMapBinder(binder, String.class, implClazz)
                .addBinding(implName).to(implClazz).in(Scopes.SINGLETON);
    }

    public static <T> void bind(Binder binder, Class<T> i, String propKey, String defaultImpl) {
        binder.bind(i).toProvider(new ConfiggedProvider<T>(i, propKey, defaultImpl));
    }

    private static class ConfiggedProvider<T> implements Provider<T> {

        private final String propKey;

        private final String defaultImpl;

        private final Class<T> i;

        private Properties props;

        private Injector ij;


        ConfiggedProvider(Class<T> i, String propKey, String defaultImpl) {
            this.i = i;
            this.propKey = propKey;
            this.defaultImpl = defaultImpl;
        }

        @Inject
        void inject(Injector injector, Properties props) {
            this.ij = injector;
            this.props = props;
        }


        @Override
        public T get() {
            String implName = (String) props.get(propKey);
            if (implName == null) {
                implName = defaultImpl;
            }

            final Map<String, T> impls;
            final ParameterizedType mapType = Types.mapOf(String.class, i);

            impls = (Map<String, T>) ij.getInstance(Key.get(mapType));

            return impls.get(implName);
        }
    }

}
