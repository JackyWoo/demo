package woo.demo.joda;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.junit.Test;

/**
 * Created by wujianchao on 2019/6/10.
 */
public class JodeTime {

    @Test
    public void test(){
        Period period = new Period("P3D");
        DateTime now = DateTime.now();
        final Interval currInterval = new Interval(period, now);
        System.out.println(currInterval);

    }
}
