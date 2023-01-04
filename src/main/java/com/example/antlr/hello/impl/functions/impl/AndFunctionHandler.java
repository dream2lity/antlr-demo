package com.example.antlr.hello.impl.functions.impl;

import com.example.antlr.hello.HelloParser;
import com.example.antlr.hello.impl.base.FieldTypeEnum;
import com.example.antlr.hello.impl.functions.AbstractFunctionHandler;

import java.util.StringJoiner;

import static com.example.antlr.hello.HelloParser.*;
import static com.example.antlr.hello.impl.HelloConvertListener.*;

/**
 * @author chijiuwang
 */
public class AndFunctionHandler extends AbstractFunctionHandler {

    protected String delimiter = "AND";

    @Override
    public TreeNodeInfo processed() {
        if (this.expressionContexts.isEmpty()) {
            return generateTreeNode("1", FieldTypeEnum.NUMBER);
        }
        StringJoiner joiner = new StringJoiner(" \n\t" + this.delimiter + " ", "cast(((\n\t", " \n\t)) as signed)");
        TreeNodeInfo expr;
        for (HelloParser.ExpressionContext expressionContext : this.expressionContexts) {
            expr = getParseTreeNode(expressionContext);
            if (isLogicalExpression(expr)) {
                joiner.add(isSimpleNumberContext(expr) ? expr.getExpr() + " <> 0" : expr.getExpr());
            } else {
                throw new IllegalArgumentException(this.delimiter + ":参数类型必须为布尔类型或数值类型");
            }
        }
        return generateTreeNode(joiner.toString(), FieldTypeEnum.NUMBER);
    }

    protected boolean isLogicalExpression(TreeNodeInfo treeNodeInfo) {
        return treeNodeInfo.getCtx() instanceof HelloParser.BooleanExpressionContext ||
                treeNodeInfo.getCtx() instanceof HelloParser.BooleanContext ||
                treeNodeInfo.getType() == FieldTypeEnum.NUMBER;
    }

    protected boolean isSimpleNumberContext(TreeNodeInfo treeNodeInfo) {
        ExpressionContext ctx = treeNodeInfo.getCtx();
        if (ctx instanceof NumberContext) {
            return true;
        } else if (ctx instanceof ParenExpressionContext) {
            ExpressionContext expression = ((ParenExpressionContext) ctx).expression();
            return expression instanceof NumberContext;
        }
        return false;
    }
}
