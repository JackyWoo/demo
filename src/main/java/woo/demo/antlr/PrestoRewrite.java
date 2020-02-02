package woo.demo.antlr;

import woo.demo.TestParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.stream.Collectors;

/**
 * Created by wujianchao on 2019/9/10.
 */
public class PrestoRewrite extends SqlRewrite.Rewrite {

    PrestoRewrite(TokenStream tokenStream) {
        super(tokenStream);
    }

    /**
     * Hive support regexp and rlike operators, but Presto does not
     *
     * Resolution:
     *  col regexp '.*' =>  regexp_like(col, '.*')
     *  col rlike '.*' =>  regexp_like(col, '.*')
     *  col not regexp '.*' =>  not regexp_like(col, '.*')
     *  col not rlike '.*' =>  not regexp_like(col, '.*')
     */
    @Override
    public void enterPredicate(TestParser.PredicateContext ctx) {

        String kind = ctx.kind.getText().toUpperCase();
        if("REGEXP".equals(kind) || "RLIKE".equals(kind)){
            ParserRuleContext parent = ctx.getParent();
            StringBuffer replacement = new StringBuffer();

            if(ctx.getChildCount() == 3 && "not".equals(ctx.getChild(0).getText().toLowerCase())){
                // 'str' not regexp '.*'
                replacement.append(ctx.getChild(0).getText()).append(" ");
            }

            replacement.append("regexp_like(")
                    .append(childAsText(parent, 0))
                    .append(", ")
                    .append(nodeText(ctx.pattern))
                    .append(")");
            tokenStreamRewriter.replace(parent.start, parent.stop, replacement.toString());

        }
    }
//    @Override
//    public void enterPredicate(TestParser.PredicateContext ctx) {
//
//        String kind = ctx.kind.getText().toUpperCase();
//        if("REGEXP".equals(kind) || "RLIKE".equals(kind)){
//            ParserRuleContext parent = ctx.getParent();
//            StringBuffer replacement = new StringBuffer();
//
//            if(ctx.getChildCount() == 3 && "not".equals(ctx.getChild(0).getText().toLowerCase())){
//                // 'str' not regexp '.*'
//                replacement.append(ctx.getChild(0).getText()).append(" ");
//            }
//
//            replacement.append("regexp_like(")
//                    .append(childAsText(parent, 0))
//                    .append(", ")
//                    .append(nodeText(ctx.pattern))
//                    .append(")");
//            tokenStreamRewriter.replace(parent.start, parent.stop, replacement.toString());
//
//        }
//
//    }

    /**
     * Hive support % operator, but Presto does not.
     *
     * Resolution:
     *  1 % 2 => MOD(1, 2)
     */
    @Override
    public void enterArithmeticBinary(TestParser.ArithmeticBinaryContext ctx) {
        String operator = ctx.operator.getText();
        if("%".equals(operator)){
            StringBuffer replacement = new StringBuffer()
                    .append("mod(")
                    .append(nodeText(ctx.left))
                    .append(", ")
                    .append(nodeText(ctx.right))
                    .append(")");
            tokenStreamRewriter.replace(ctx.start, ctx.stop, replacement.toString());
        }
    }

    /**
     * Resolution:
     *  1. array(1, 2) => array[1, 2]
     *  2. concat('a','b','c') => 'a' || 'b' || 'c'
     */
    @Override
    public void enterFunctionCall(TestParser.FunctionCallContext ctx) {

        if ("ARRAY".equals(ctx.getChild(0).getText().toUpperCase())){
            tokenStreamRewriter.replace(ctx.start.getTokenIndex() + 1, "[");
            tokenStreamRewriter.replace(ctx.stop, "]");
        }

        if ("CONCAT".equals(ctx.getChild(0).getText().toUpperCase())){
            String replacement = String.join(" || ", ctx.argument.stream()
                    .map(argument -> nodeText(argument))
                    .collect(Collectors.toList()));
            tokenStreamRewriter.replace(ctx.start, ctx.stop, replacement);
        }
    }


    /**
     * Hive identifier quoter : `
     * Presto identifier quoter : "
     *
     * Resolution:
     *  `col` => "col"
     */
    @Override
    public void enterQuotedIdentifier(TestParser.QuotedIdentifierContext ctx) {
        String identifier = ctx.getText();
        char[] chars = new char[identifier.length()];
        identifier.getChars(0, identifier.length(), chars, 0);
        chars[0] = '"';
        chars[chars.length -1] = '"';
        tokenStreamRewriter.replace(ctx.start, new String(chars));
    }

    /**
     * Hive identifier can start with digit
     * Presto can not
     */
    @Override
    public void enterUnquotedIdentifier(TestParser.UnquotedIdentifierContext ctx) {
        String identifier = ctx.getText();
        char firstChar = identifier.charAt(0);
        if(firstChar >= '0' && firstChar <= '9'){
            tokenStreamRewriter.replace(ctx.start, "\"" + identifier + "\"");
        }
    }

    private String tokenText(Token token){
        return token.getText();
    }

    private String nodeText(ParseTree node){
        return node.getText();
    }

    private String childAsText(ParseTree node, int childIndex){
        return node.getChild(childIndex).getText();
    }

    //TODO LATERAL VIEW explode(scores) t AS score => CROSS JOIN UNNEST(scores) AS t (score)

}
