package demo.antlar.calc;

import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;

/**
 * Created by wujianchao on 2019/7/19.
 */
public class InsertCommentListener extends CalcBaseListener {

    private final TokenStreamRewriter tokenStreamRewriter;

    public InsertCommentListener(TokenStream tokenStream){
        tokenStreamRewriter = new TokenStreamRewriter(tokenStream);
    }

    @Override
    public void enterCalc(CalcParser.CalcContext ctx) {
//        tokenStreamRewriter.replace(ctx.getStart(), "100");
        tokenStreamRewriter.insertAfter(ctx.getStart(), "+ 5");
        System.out.println(tokenStreamRewriter.getText());
    }

    public void print(){
        System.out.println(tokenStreamRewriter.getText());
    }

    public TokenStream getRewriteTokenStream() {
        return tokenStreamRewriter.getTokenStream();
    }

    public String getRewriteExpr(){
        return tokenStreamRewriter.getText();
    }

}
