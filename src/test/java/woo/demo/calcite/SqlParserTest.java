package woo.demo.calcite;

import org.apache.calcite.avatica.util.Quoting;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.rel.RelRoot;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.validate.SqlConformanceEnum;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;
import org.apache.calcite.tools.Planner;
import org.apache.calcite.tools.RelConversionException;
import org.apache.calcite.tools.ValidationException;
import org.apache.calcite.util.Sources;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by wujianchao on 2019/6/6.
 */
public class SqlParserTest {


    /**
     * scenario:
     *      Table : student -> id, name
     *      Table : scores -> id, user_id, score
     */

    SqlParser.Config config;

    @Before
    public void setup(){
        config = SqlParser.configBuilder()
                .setCaseSensitive(true)
                .setQuoting(Quoting.BACK_TICK)
                .setConformance(SqlConformanceEnum.PRAGMATIC_2003)
                .build();

    }


    @Test
    public void test() throws SqlParseException, ValidationException, IOException, SQLException, ClassNotFoundException, RelConversionException {
//        String sql = "select DEPTNO, NAME from SALES.DEPTS where NAME=10 order by DEPTNO desc";
        String sql = "select * from edw.f_agt_t01_cust_sso_credit_detail\n" +
                "where ramarks<>'' and business_name like '%OPPO+%'\n" +
                "  and `date`(cast(create_time as timestamp)) >= date '2019-07-01'\n" +
                "limit 100";

//        SqlParser parser = SqlParser.create(sql, config);
//        SqlNode sqlTree = parser.parseQuery();

        CalciteConnection connection = getConnection();

        Frameworks.ConfigBuilder builder = Frameworks.newConfigBuilder()
                .defaultSchema(connection.getRootSchema())
                .parserConfig(config);

        FrameworkConfig config = builder.build();
        Planner planner = Frameworks.getPlanner(config);

        SqlNode sqlTree = planner.parse(sql);

        System.out.println(sqlTree);

        SqlNode validatedSqlTree = planner.validate(sqlTree);

        RelRoot relRoot = planner.rel(validatedSqlTree);

        System.out.println(relRoot);
//        RelNode relNode = planner.transform();

    }

    private CalciteConnection getConnection() throws SQLException, ClassNotFoundException {

        Class.forName("org.apache.calcite.jdbc.Driver");

        Properties info = new Properties();
        info.setProperty("model", resourcePath("model.json"));

        CalciteConnection connection = (CalciteConnection) DriverManager
                .getConnection("jdbc:calcite:", info);
//        ModelHandler handler = new ModelHandler(connection, resourcePath("model.json"));
        return connection;
    }


    private String resourcePath(String path) {
        return Sources.of(SqlParserTest.class.getResource("/" + path)).file().getAbsolutePath();
    }
}
