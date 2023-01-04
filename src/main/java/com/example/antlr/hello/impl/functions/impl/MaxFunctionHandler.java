package com.example.antlr.hello.impl.functions.impl;

import com.example.antlr.hello.HelloParser;
import com.example.antlr.hello.impl.HelloConvertListener;
import com.example.antlr.hello.impl.base.FieldTypeEnum;
import com.example.antlr.hello.impl.functions.AbstractFunctionHandler;

import java.util.StringJoiner;

import static com.example.antlr.hello.impl.HelloConvertListener.*;

/**
 * @author chijiuwang
 */
public class MaxFunctionHandler extends AbstractFunctionHandler {

    private static final int MIN_ARG_SIZE = 1;

    protected String functionName = "MAX";
    protected String functionRealName = "GREATEST";

    @Override
    public HelloConvertListener.TreeNodeInfo processed() {
        if (this.expressionContexts.size() < MIN_ARG_SIZE) {
            throw new IllegalArgumentException(functionName + "(数值,...):不符合的参数要求");
        }
        TreeNodeInfo expr;
        StringJoiner joiner = new StringJoiner(", \n\t", functionRealName + "( \n\t", " \n\t)");
        for (HelloParser.ExpressionContext expressionContext : this.expressionContexts) {
            expr = getParseTreeNode(expressionContext);
            if (expr.getType() != FieldTypeEnum.NUMBER) {
                throw new IllegalArgumentException(functionName + "(数值,...):不符合的参数要求");
            }
            joiner.add(expr.getRealExpr());
        }
        return generateTreeNode(joiner.toString(), FieldTypeEnum.NUMBER);
    }
}
