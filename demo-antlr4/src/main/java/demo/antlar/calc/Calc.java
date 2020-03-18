package demo.antlar.calc;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.java2d.pipe.SpanIterator;

/**
 * Created by wujianchao on 2019/7/19.
 */
public class Calc {

    private static Logger logger = LoggerFactory.getLogger(Calc.class);

    public static void main(String[] args) {

        String expr = "2 *(1 + 1)";

        //generate token stream
        demo.antlar.calc.CalcLexer lexer = new CalcLexer(CharStreams.fromString(expr));
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        //generate AST
        CalcParser parser = new CalcParser(tokens);
        parser.addParseListener(new InsertCommentListener(tokens));
        ParseTree tree = parser.calc();

        System.out.println(tree.toStringTree(parser));
        System.out.println();

        //Walk through with visitor
        System.out.println("Visitor:");
        CalcVisitor<Integer>  visitor = new CalcRealVisitor();
        int r = visitor.visit(tree);
        System.out.println(r);

//        System.out.println("Listener:");
//        ParseTreeWalker walker = new ParseTreeWalker();
//        InsertCommentListener listener = new InsertCommentListener(tokens);
//        walker.walk(listener, tree);

        logger.info(tree.toStringTree(parser));
    }

}
