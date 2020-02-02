package woo.demo.polymorphic.jackson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;

/**
 * Created by wujianchao on 2020/1/19.
 */
public class Test {

    private static byte[] NULL_DATA = new byte[]{};

    public static void main(String[] args) {


        // 1. use defaultImpl
        objectMapper().convertValue(props().build(), Storage.class).push(NULL_DATA);

        // 2. use type implementation
        ImmutableMap.Builder<String, String> hdfsProps = props();
        hdfsProps.put("type", "hdfs");
        objectMapper().convertValue(hdfsProps.build(), Storage.class).push(NULL_DATA);


        // 3. use type implementation
        ImmutableMap.Builder<String, String> s3Props = props();
        s3Props.put("type", "s3");
        objectMapper().convertValue(s3Props.build(), Storage.class).push(NULL_DATA);


        // 4. use JsonTypeName annotation

        // first register class
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerSubtypes(RedisStorage.class);

        ImmutableMap.Builder<String, String> redisProps = props();
        redisProps.put("type", "redis");
        objectMapper.convertValue(redisProps.build(), Storage.class).push(NULL_DATA);

    }

    public static ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        return objectMapper;
    }

    public static ImmutableMap.Builder<String, String> props(){
        ImmutableMap.Builder<String, String> props = ImmutableMap.<String, String>builder()
                .put("path", "/path/to/storage")
                ;
        return props;
    }
}
