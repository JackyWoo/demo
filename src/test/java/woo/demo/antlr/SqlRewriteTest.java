package woo.demo.antlr;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by wujianchao on 2019/9/16.
 */
public class SqlRewriteTest {

    @Test
    public void rlikepRewriteWithQuotedIdentifierTest(){
        String sql = "`col` rlike '.*'";
        String rewriteSql = SqlRewrite.prestoSqlDialect(sql);
        String expected = "regexp_like(\"col\", '.*')";
        assertEquals(expected, rewriteSql);
    }
}
