package fr.devoxx.grandmere;

import com.devoxx.GrandMereBaseVisitor;
import com.devoxx.GrandMereParser;

public class GrandMereVisitor extends GrandMereBaseVisitor<String> {

    @Override
    public String visitAtomExpr(GrandMereParser.AtomExprContext ctx) {
        return ctx.getText();
    }
}
