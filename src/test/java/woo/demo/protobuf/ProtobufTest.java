package woo.demo.protobuf;

import org.junit.Test;
import woo.demo.ProtoPerson;
import woo.demo.ProtoPerson.Person;

/**
 * Created by wujianchao on 2020/1/2.
 */
public class ProtobufTest {

    Person john = Person.newBuilder()
            .setId(1234)
            .setName("John Doe")
            .setEmail("jdoe@example.com")
            .addPhones(
                    Person.PhoneNumber.newBuilder()
                            .setNumber("555-4321")
                            .setType(Person.PhoneType.HOME))
            .build();


    @Test
    public void tt(){
        String dayno= "201";
        System.out.println(String.valueOf(Double.parseDouble(dayno)).equals(dayno));
        System.out.println(Double.parseDouble(dayno));
        System.out.println(String.valueOf(Double.parseDouble(dayno)));

    }
}
