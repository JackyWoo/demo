package woo.demo.orc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.exec.vector.BytesColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.ColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.LongColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.VectorizedRowBatch;
import org.apache.hadoop.hive.ql.io.sarg.PredicateLeaf;
import org.apache.hadoop.hive.ql.io.sarg.SearchArgument;
import org.apache.hadoop.hive.ql.io.sarg.SearchArgumentFactory;
import org.apache.hadoop.io.Text;
import org.apache.orc.CompressionKind;
import org.apache.orc.OrcFile;
import org.apache.orc.Reader;
import org.apache.orc.RecordReader;
import org.apache.orc.TypeDescription;
import org.apache.orc.Writer;
import org.apache.orc.impl.StringRedBlackTree;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wujianchao on 2019/12/11.
 */
public class OrcDictTest {

    /**
     */
    @Test
    public void dictTest() throws IOException {
        StringRedBlackTree dict = new StringRedBlackTree(10);
        List<Integer> rows = new ArrayList<>();
        rows.add(dict.add("a"));
        rows.add(dict.add("d"));
        rows.add(dict.add("c"));
        rows.add(dict.add("d"));
        rows.add(dict.add("b"));

        dict.visit((context) -> System.out.println(context.getText()));

        Text result = new Text();
        dict.getText(result, 0);
        System.out.println(result.toString());

        System.out.println(rows);
    }

    @Test
    public void orcTest() throws IOException {
        Configuration conf = new Configuration();
        TypeDescription schema = TypeDescription.fromString("struct<city:string,count:int>");
        new File("/tmp/test.orc").delete();
        Writer writer = OrcFile.createWriter(new Path("/tmp/test.orc"),
                OrcFile.writerOptions(conf)
                        .compress(CompressionKind.NONE)
                        .setSchema(schema));
        VectorizedRowBatch batch = schema.createRowBatch();

        BytesColumnVector first = (BytesColumnVector) batch.cols[0];
        LongColumnVector second = (LongColumnVector) batch.cols[1];

        System.out.println(batch.getMaxSize());

        first.setVal(0, "beijing".getBytes());
        first.setVal(1, "shanghai".getBytes());
        first.setVal(2, "hangzhou".getBytes());
        first.setVal(3, "beijing".getBytes());
        first.setVal(4, "beijing".getBytes());

        second.vector[0]=1;
        second.vector[1]=1;
        second.vector[2]=1;
        second.vector[3]=1;
        second.vector[4]=1;

        batch.size = 5;
        if (batch.size != 0) {
            writer.addRowBatch(batch);
            batch.reset();
        }

        writer.close();
    }

    @Test
    public void orcReadTest() throws IOException {
        Configuration conf = new Configuration();
        Path testFilePath = new Path("file:///tmp/test.orc");
        Reader reader = OrcFile.createReader(testFilePath,
                OrcFile.readerOptions(conf));
        RecordReader rows = reader.rows();
        VectorizedRowBatch batch = reader.getSchema().createRowBatch();
        while (rows.nextBatch(batch)) {
            System.out.println(batch);
        }
        rows.close();
    }

    @Test
    public void orcReadWithFilterTest() throws IOException {
        Configuration conf = new Configuration();
        Path testFilePath = new Path("file:///tmp/test.orc");

        Reader reader = OrcFile.createReader(testFilePath, OrcFile.readerOptions(conf));

        SearchArgument searchArgument = SearchArgumentFactory
                .newBuilder()
                .startAnd()
                .in("city", PredicateLeaf.Type.STRING, "beijing")
                .equals("city", PredicateLeaf.Type.STRING, "beijing")
                .end()
                .build();

        Reader.Options readerOptions = reader.options()
                .include(new boolean[]{false, true, false})
                .searchArgument(searchArgument, new String[]{"city"});

        RecordReader rows = reader.rows(readerOptions);
        VectorizedRowBatch batch = reader.getSchema().createRowBatch();
        while (rows.nextBatch(batch)) {
            System.out.println(batch);
        rows.close();
    }
    }

}
