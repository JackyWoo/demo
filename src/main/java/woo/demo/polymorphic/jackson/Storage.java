package woo.demo.polymorphic.jackson;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apache.hadoop.fs.Hdfs;

/**
 * Created by wujianchao on 2020/1/19.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,     // use logical name to identify class type
        property = "type",              // property "type" represent class type
        visible = false,                // weather property is an object field
        defaultImpl = HdfsStorage.class // if set, Jackson will try to create the implementation when no type properties.
)

@JsonSubTypes({
        @JsonSubTypes.Type(name = "hdfs", value = HdfsStorage.class),  // register implementation
        @JsonSubTypes.Type(name = "s3", value = S3Storage.class),
})
public interface Storage {
    void push(byte[] data);
}
