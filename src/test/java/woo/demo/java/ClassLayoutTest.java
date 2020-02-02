package woo.demo.java;

import io.airlift.slice.Slice;
import org.junit.Test;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;

import java.util.ArrayList;

/**
 * Created by wujianchao on 2019/11/29.
 */
public class ClassLayoutTest {

    @Test
    public void primitiveFiledTest(){
        ClassLayout layout = ClassLayout.parseClass(ClassA.class);
        System.out.println(layout.toPrintable());
        // instance 8 byte alignment
    }

    @Test
    public void objectFieldTest(){
        ClassLayout layout = ClassLayout.parseClass(ClassB.class);
        System.out.println(layout.toPrintable());
    }

    @Test
    public void objectExtensionTest(){
        ClassLayout layout = ClassLayout.parseClass(ClassC.class);
        System.out.println(layout.toPrintable());
    }


    @Test
    public void boxedTypeTest(){
        ClassLayout layout = ClassLayout.parseClass(Integer.class);
        System.out.println(layout.toPrintable());
    }

    @Test
    public void arrayTest(){
        int[] arr = {1,2,3};
        ClassLayout layout = ClassLayout.parseClass(arr.getClass());
        System.out.println(layout.toPrintable());
        System.out.println(GraphLayout.parseInstance(arr).toPrintable());
        System.out.println(GraphLayout.parseInstance(arr).totalSize());
        System.out.println(GraphLayout.parseInstance(arr).totalCount());
    }

    @Test
    public void arrayObjectTest(){
        ClassA[] arr = {new ClassA(1, 1), new ClassA(1, 1)};
        System.out.println(GraphLayout.parseInstance(arr).toPrintable());
        System.out.println(GraphLayout.parseInstance(arr).totalSize());
        System.out.println(GraphLayout.parseInstance(arr).toFootprint());
    }

    @Test
    public void collectionTest(){
        ClassLayout layout = ClassLayout.parseClass(ArrayList.class);
        System.out.println(layout.toPrintable());
    }

    @Test
    public void sliceTest(){
        ClassLayout layout = ClassLayout.parseClass(Slice.class);
        System.out.println(layout.toPrintable());
    }

    @Test
    public void stringTest(){
        ClassLayout layout = ClassLayout.parseClass(String.class);
        System.out.println(layout.toPrintable());

        String s = "123";
        System.out.println(GraphLayout.parseInstance(s).toPrintable());
        System.out.println(GraphLayout.parseInstance(s).totalSize());
    }


}
