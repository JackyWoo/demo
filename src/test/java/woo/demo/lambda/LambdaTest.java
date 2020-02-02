package woo.demo.lambda;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by wujianchao on 2019/3/12.
 */
public class LambdaTest {

    @Test
    public void test1(){
        // 函数式接口：只包含一个抽象方法的接口
        // 函数是接口可以简单写
        Runnable r = () -> System.out.println("hello world");
    }

    @Test
    public void test2(){
        List<String> list = Arrays.asList("1", "2", "3");
        // for each 函数

        // 函数试接口，常见函数式接口：
        // ava.util.function.Consumer<T> 有一个输入参数，没有返回值
        // BiConsumer<T, I> 有两个个输入参数，没有返回值
        // java.util.function.Supplier<T> 没有输入参数，只有返回值
        // java.util.function.Predicate<T> 有一个输入参数，返回 true or false
        // java.util.function.Function<T,R> 有一个输入参数，和一个返回值
        // BiFunction<I, T, R> 有两个输入参数，和一个返回值

        list.forEach(s -> System.out.println(s));
    }

    @Test
    public void test4(){
        List<String> list = Arrays.asList("1", "2", "3");
        // 普通方法形态的方法引用， 方法需要一个参数，一个返回值。
        // 这种类型的方法引用实质上是：Function<R, T>
        list.stream().map(String::toUpperCase).forEach(x -> System.out.println(x));
        // 也可以用如下的写法
        Function<String, String> toUpperCase = String::toUpperCase;
        list.stream().map(toUpperCase).forEach(x -> System.out.println(x));

    }

    @Test
    public void test5(){
        List<String> list = Arrays.asList("1", "2", "3");
        // 对象形态的方法引用， 方法需要一个参数，没有返回值
        // 这种类型的方法引用实质上是：Consumer<R>
        Consumer<String> consumer = System.out::println;
        list.stream().forEach(consumer);
    }

    @Test
    public void test6(){
        List<String> list = Arrays.asList("1", "2", "3");
        // 静态方法形态的方法引用， 方法没有一个参数，是否有返回值都可以
        // 这种类型的方法引用实质上是：Consumer<R>
        Consumer<String> consumer = String::valueOf;
        list.stream().forEach(consumer);
    }



}
