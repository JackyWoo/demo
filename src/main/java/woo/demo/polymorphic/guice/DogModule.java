package woo.demo.polymorphic.guice;

import com.google.inject.Binder;
import com.google.inject.Module;
import woo.demo.guice.obj.animal.Animal;
import woo.demo.guice.obj.animal.Dog;

/**
 * Created by wujianchao on 2020/2/3.
 */
public class DogModule implements Module {
    @Override
    public void configure(Binder binder) {
        GuicePoly.registerImpl(binder, Animal.class, "dog", Dog.class);
    }
}
