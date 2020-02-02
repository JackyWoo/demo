package woo.demo.lambda;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by wujianchao on 2019/3/12.
 */
public class StreamTest {

    /**
     * 如何构建Stream
     */
    @Test
    public void buildStreamTest() throws FileNotFoundException {
        // 1. from items
        Stream s1 = Stream.of("a", "b", "c");

        // 2. from arrays
        String [] strArray = new String[] {"a", "b", "c"};
        Stream s2 = Stream.of(strArray);
        s2.forEach(System.out::println);

        // 3. from Collections
        List<String> list = Arrays.asList("a", "b", "c");
        Stream s3 = list.stream();

        // 4. from BufferedReader
//        BufferedReader br = new BufferedReader(new FileReader(""));
//        Stream s4 = br.lines();

        // 5. others
        //Random.ints()
        //BitSet.stream()
        //Pattern.splitAsStream(java.lang.CharSequence)
        //JarFile.stream()
    }

    /**
     * 如何构建Stream
     * 为了避免boxing和unboxing，额外提供三种类型的Stream
     */
    @Test
    public void otherStreamTest(){
        // 为了避免boxing和unboxing，提供了:
        // IntStream
        // LongStream
        // DoubleStream
        IntStream.of(1, 2, 3).forEach(System.out::println);
        IntStream.range(1, 3).forEach(System.out::println);

    }

    /**
     * Stream 重用
     * 一个Stream一旦调用terminal operation后就不能复用了。
     */
    @Test
    public void streamReuse(){
        Supplier<Stream<String>> streamSupplier = () -> Stream.of("a", "b", "c");
        streamSupplier.get().forEach(System.out::print);
    }

    /**
     * 流转换为其它数据结构
     */
    @Test
    public void transformStreamTest(){

        Supplier<Stream<String>> streamSupplier = () -> Stream.of("a", "b", "c");

        // to 数组
        Object[] arr = streamSupplier.get().toArray();

        // to List
        streamSupplier.get().collect(Collectors.toList());

        // to set
        streamSupplier.get().collect(Collectors.toSet());

        // to collection
        streamSupplier.get().collect(Collectors.toCollection(HashSet::new));

        // to 普通对象
        String str = streamSupplier.get().collect(Collectors.joining());
    }

    /**
     * Stream的执行顺序，参考spark、presto等查询引擎
     *      1) filter map等可以组成pipeline的operator
     *      2) sort reduce collect等涉及到
     */
    @Test
    public void executionTest(){
        Supplier<Stream<String>> streamSupplier = () -> Stream.of("a", "b", "c");

        streamSupplier.get().filter(s -> {
            System.out.println("filter : " + s);
            return true;
        }).forEach(s -> {
            System.out.println("forEach : " + s);
        });

        System.out.println("\n----\n");

        streamSupplier.get().filter(s -> {
            System.out.println("filter : " + s);
            return true;
        }).sorted((x, y) -> {
            System.out.println("sorted : " + x);
            return x.getBytes().length - y.getBytes().length;
        }).forEach(s -> {
            System.out.println("forEach : " + s);
        });

    }



    /**
     * 流上的操作有两种：
     *      Intermediate：
     *          map (mapToInt, flatMap 等)、 filter、 distinct、 sorted、 peek、 limit、 skip、 parallel、 sequential、 unordered
     *      Terminal：
     *          forEach、 forEachOrdered、 toArray、 reduce、 collect、 min、 max、 count、 anyMatch、 allMatch、 noneMatch、 findFirst、 findAny、 iterator
 *          Short-circuiting：
     *          anyMatch、 allMatch、 noneMatch、 findFirst、 findAny、 limit
     */
    @Test
    public void streamOperatorTest(){

        List<Integer> nums = Arrays.asList(1, 2, 3, 4);

        //map : Function<I, R>
        List<Integer> squareNums = nums.stream().
                map(n -> n * n).
                collect(Collectors.toList());

        //flatMap : 将Stream扁平化
        Stream<List<Integer>> inputStream = Stream.of(
                Arrays.asList(1),
                Arrays.asList(2, 3),
                Arrays.asList(4, 5, 6)
        );
        Stream<Integer> outputStream = inputStream.
                flatMap((childList) -> childList.stream());

        // filter

        // skip

        // limit

        // sort

        // forEach : Consumer<R>
        // forEach是terminal operation，操作后Stream就关闭了，不能在使用
        // 可以用intermediate operation "peak" 替换
        Stream<String> ss = Stream.of("a", "b", "c");
        ss.peek(System.out::println).reduce((x,y) -> x+y).get();

    }


    /**
     * 合并map
     */
    @Test
    public void mergeMapTest(){
        Map<String, Integer> m1 = new HashMap<>();
        Map<String, Integer> m2 = new HashMap<>();
        m1.put("a", 1);
        m1.put("b", 2);
        m2.put("a", 1);
        m2.put("d", 2);

        Map<String, Integer> mx = Stream.of(m1, m2)
                //流操作是面向线性结构的，而不是map
                .map(Map::entrySet)
                //flat stream
                .flatMap(Set::stream)
                //merge entry set and handle conflict
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> x + y));

        System.out.println(mx);

    }

    /**
     * reduce可以归纳数据。
     * 它接受BinaryOperator函数，返回值与输入数据一致。
     * reduce的语义都可以用collector实现。
     */
    @Test
    public void reduceTest(){

        Supplier<Stream<Integer>> s = () -> Stream.of();

//        //如果流为空则返回默认值 1
//        int r = s.get().reduce((x, y) -> x + y).get();
//
//        r = s.get().reduce(1,  (x, y) -> x + y);
//
//        System.out.println(r);

//        s.get().mapToInt(x -> x.intValue()).sum();
    }

    /**
     * 1. 将Stream转化为不同的结构
     * 2. 实现类似group by功能
     */
    @Test
    public void collectTest(){

        List<Person> persons = Arrays.asList(
            new Person("Max", 18),
            new Person("Peter", 23),
            new Person("Pamela", 23),
            new Person("David", 12)
        );

        //转化为List
        List<Person> filtered = persons.stream().filter(p -> p.age > 20).collect(Collectors.toList());

        // 实现 group by
        Map<Integer, List<Person>> r = persons.stream().collect(Collectors.groupingBy(Person::getAge));

        // 实现多级 group by
        Map<String, Map<Integer, List<Person>>> rr = persons.stream().
                collect(Collectors.groupingBy(Person::getName, Collectors.groupingBy(Person::getAge)));

        //partition
        // 一种特殊的group by，由Predicate函数分为两组
        Map<Boolean, List<Person>> rrrr = persons.stream().
                collect(Collectors.partitioningBy((Person x) -> x.getName().startsWith("P")));

        // 实现avg age
        persons.stream().collect(Collectors.averagingInt(Person::getAge));

        // 查找最大值
        persons.stream().collect(Collectors.maxBy((x,y) -> x.getAge() - y.getAge()));

        //实现distinct
        persons.stream().distinct().forEach(p -> System.out.println(p.name));

        // 链接name
        String rrr = persons.stream().map(Person::getName).collect(Collectors.joining(" "));
        System.out.println(rrr);
        rrr = persons.stream().map(Person::getName).collect(Collectors.joining(" and ", "I am ", ", hello everyone."));
        System.out.println(rrr);

        // 全面统计
        // IntSummaryStatistics{count=4, sum=76, min=12, average=19.000000, max=23}
        IntSummaryStatistics summaryStatistics = persons.stream().collect(Collectors.summarizingInt(Person::getAge));
        System.out.println(summaryStatistics);

    }

    /**
     * 自定义collector,实现work count功能
     *
     * Collector是一些列函数的集合，这些函数由Stream.collect()方法组装在一起实现了类似Hadoop MapReduce的功能。
     * Collector的函数包含：
     *      1 supplier：产生一个用以存放最终结果的容器
     *      2 accumulator：归并输入的数据，结果传送给combiner处理
     *      3 combiner：类似Hadoop的combiner，用来合并部分结果
     *      4 最终结果转换，转换基于combiner合并后的结果
     */
    @Test
    public void customCollectorTest(){

        Stream<String> stream = Stream.of("Welcome", "to", "Beijing", "Beijing", "is", "a", "nice", "place");

        Collector<String, Map<String, Integer>, Map<String, Integer>> wordCountCollector = Collector.of(
                HashMap::new,
                (Map<String, Integer> map, String world) -> {
//                    map.compute(world, (k, v) -> v + 1);
                    if(map.get(world) == null){
                        map.put(world, 1);
                    }else{
                        map.put(world, map.get(world) + 1);
                    }
                },
                (Map<String, Integer>left, Map<String, Integer> right) -> {
                    return Stream.of(left, right)
                            .map(Map::entrySet)
                            .flatMap(Set::stream)
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> x + y));
                }
        );

        Map<String, Integer> count = stream.collect(wordCountCollector);
        System.out.println(count);

    }


    /**
     * 并行Stream
     * ForkJoinPool.commonPool()
     */
    @Test
    public void parallelStreamTest(){
        ForkJoinPool.commonPool();
        Arrays.asList("a", "b", "c", "d", "e")
                .parallelStream()
                .filter(s -> {
                    System.out.format("filter: %s [%s]\n",
                            s, Thread.currentThread().getName());
                    return true;
                })
                .map(s -> {
                    System.out.format("map: %s [%s]\n",
                            s, Thread.currentThread().getName());
                    return s.toUpperCase();
                })
                .forEach(s -> System.out.format("forEach: %s [%s]\n",
                        s, Thread.currentThread().getName()));
    }

    /**
     * 加入sort后将打乱之前的线程任务分配，与执行顺序
     */
    @Test
    public void parallelStreamWithSortTest(){
        Arrays.asList("a", "b", "c", "d", "e")
                .parallelStream()
                .filter(s -> {
                    System.out.format("filter: %s [%s]\n",
                            s, Thread.currentThread().getName());
                    return true;
                })
                .map(s -> {
                    System.out.format("map: %s [%s]\n",
                            s, Thread.currentThread().getName());
                    return s.toUpperCase();
                })
                .sorted((s1, s2) -> {
                    System.out.format("sort: %s <> %s [%s]\n",
                            s1, s2, Thread.currentThread().getName());
                    return s1.compareTo(s2);
                })
                .forEach(s -> System.out.format("forEach: %s [%s]\n",
                        s, Thread.currentThread().getName()));
    }

    /**
     * 加入sort后将打乱之前的线程任务分配，与执行顺序
     * 由此可见算子是按顺序执行的。
     */
    @Test
    public void parallelStreamWithLimitTest(){
        Arrays.asList("a", "b", "c", "d", "e")
                .parallelStream()
                .filter(s -> {
                    System.out.format("filter: %s [%s]\n",
                            s, Thread.currentThread().getName());
                    return true;
                })
                .map(s -> {
                    System.out.format("map: %s [%s]\n",
                            s, Thread.currentThread().getName());
                    return s.toUpperCase();
                })
                .limit(2)
                .forEach(s -> System.out.format("forEach: %s [%s]\n",
                        s, Thread.currentThread().getName()));
    }

    /**
     * Stream操作没有查询引擎的操作数下推等机制
     * 所以需要严格注意操作顺序
     */
    @Test
    public void limitTest(){
        Arrays.asList("a", "b", "c", "d", "e")
                .stream()
                .filter(s -> {
                    System.out.format("filter: %s \n", s);
                    return true;
                })
                .map(s -> {
                    System.out.format("map: %s \n", s);
                    return s.toUpperCase();
                })
                .limit(2)
                .forEach(s -> System.out.format("forEach: %s \n", s));
    }

}
