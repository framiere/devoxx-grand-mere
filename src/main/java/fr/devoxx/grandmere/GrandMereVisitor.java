package fr.devoxx.grandmere;

import com.devoxx.GrandMereBaseVisitor;
import com.devoxx.GrandMereParser;

public class GrandMereVisitor extends GrandMereBaseVisitor<String> {

    @Override
    public String visitAtomExpr(GrandMereParser.AtomExprContext ctx) {
        return "Double.valueOf(" + ctx.getText() + ")";
    }

    @Override
    public String visitUnaryMinusExpr(GrandMereParser.UnaryMinusExprContext ctx) {
        return "-" + visit(ctx.expr());
    }

    @Override
    public String visitUnaryPlusExpr(GrandMereParser.UnaryPlusExprContext ctx) {
        return "+" + visit(ctx.expr());
    }

    @Override
    public String visitHighPriorityOperationExpr(GrandMereParser.HighPriorityOperationExprContext ctx) {
        return "(" + visit(ctx.expr(0)) + ")." + visit(ctx.high_priority_operation()) + "(" + visit(ctx.expr(1)) + ")";
    }

    @Override
    public String visitHigh_priority_operation(GrandMereParser.High_priority_operationContext ctx) {
        switch (ctx.getText()) {
            case "*":
                return "multiply";
            case "/":
                return "divite";
            default:
                throw new RuntimeException("Could not find operation " + ctx.getText());
        }
    }

    @Override
    public String visitLowPriorityOperationExpr(GrandMereParser.LowPriorityOperationExprContext ctx) {
        return "(" + visit(ctx.expr(0)) + ")." + visit(ctx.low_priority_operation()) + "(" + visit(ctx.expr(1)) + ")";
    }

    @Override
    public String visitLow_priority_operation(GrandMereParser.Low_priority_operationContext ctx) {
        switch (ctx.getText()) {
            case "+":
                return "add";
            case "-":
                return "minus";
            default:
                throw new RuntimeException("Could not find operation " + ctx.getText());

        }
    }
}
