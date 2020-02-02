package woo.demo.antlr;

import com.google.common.collect.ImmutableList;
import woo.demo.TestBaseListener;
import woo.demo.TestBaseVisitor;
import woo.demo.TestLexer;
import woo.demo.TestParser;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Function;

/**
 * Rewrite Hive sql to other dialect such as Presto sql.
 *
 * Created by wujianchao on 2019/9/10.
 */
public class SqlRewrite {

    private static Logger LOGGER = LoggerFactory.getLogger(SqlRewrite.class);


    public static String prestoSqlDialect(String sql){
        return rewrite(sql, ImmutableList.of(PrestoRewrite::new));
    }

    private static String rewrite(String sql, List<Function<TokenStream, Rewrite>> rewrites){
        long startTime = System.currentTimeMillis();
        String rewriteSql = sql;

        for(Function<TokenStream, Rewrite> rewrite : rewrites){
            rewriteSql = rewrite(rewriteSql, rewrite);
        }

        long endTime = System.currentTimeMillis();
        LOGGER.debug("sql rewrite time cost {} ms", (endTime - startTime));

        return rewriteSql;
    }

    private static String rewrite(String sql, Function<TokenStream, Rewrite> rewriteSupplier) {
        String rewriteName = "";
        try {
            //generate token stream
            TestLexer lexer = new TestLexer(new CaseInsensitiveStream(CharStreams.fromString(sql)));
            TokenStream tokenStream = new CommonTokenStream(lexer);

            Rewrite rewrite = rewriteSupplier.apply(tokenStream);
            rewriteName = rewrite.getClass().getSimpleName();

            //generate AST
            TestParser parser = new TestParser(tokenStream);
            parser.removeErrorListeners();
            parser.addErrorListener(new BaseErrorListener(){
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer,
                        Object offendingSymbol,
                        int line,
                        int charPositionInLine,
                        String msg,
                        RecognitionException e){
                    System.out.println("parser");
                    System.out.println("line"+line+":"+charPositionInLine+" " +msg);
                    throw new RuntimeException(e);
                }
            });
            ParseTree ast = parser.booleanExpression();

            new ParseTreeWalker().walk(rewrite, ast);
            return rewrite.rewritedSql();

        }catch (Exception e){
            LOGGER.warn(String.format("%s failed to rewrite sql", rewriteName), e);
            return sql;
        }
    }

    abstract static class Rewrite extends TestBaseListener {

        protected TokenStreamRewriter tokenStreamRewriter;

        Rewrite(TokenStream tokenStream){
            //generate token stream
            this.tokenStreamRewriter = new TokenStreamRewriter(tokenStream);
        }

        protected String rewritedSql(){
            return tokenStreamRewriter.getText();
        }
    }

    abstract static class Visitor extends TestBaseVisitor<String> {

        @Override
        public String visitArithmeticBinary(TestParser.ArithmeticBinaryContext ctx) {
            return super.visitArithmeticBinary(ctx);
        }

    }


}
