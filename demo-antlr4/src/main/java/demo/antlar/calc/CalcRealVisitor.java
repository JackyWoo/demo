package demo.antlar.calc;

/**
 * Created by wujianchao on 2019/7/19.
 */
public class CalcRealVisitor extends CalcBaseVisitor<Integer> {

    @Override
    public Integer visitDigital(CalcParser.DigitalContext ctx) {
        return Integer.parseInt(ctx.DIGITAL().getText());
    }

    @Override
    public Integer visitMulDiv(CalcParser.MulDivContext ctx) {
        int left = visit(ctx.expr(0));
        int right = visit(ctx.expr(1));
        if(ctx.op.getType() == CalcParser.MUL) {
            return left * right;
        }else {
            return left / right;
        }
    }

    @Override
    public Integer visitAddSub(CalcParser.AddSubContext ctx) {
        int left = visit(ctx.expr(0));
        int right = visit(ctx.expr(1));
        if(ctx.op.getType() == CalcParser.ADD) {
            return left + right;
        }else {
            return left - right;
        }
    }

    @Override
    public Integer visitChildExpr(CalcParser.ChildExprContext ctx) {

        return visit(ctx.expr());
    }
}
