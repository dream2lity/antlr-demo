package com.example.antlr.hello.impl.functions.impl;

import com.example.antlr.hello.impl.HelloConvertListener;
import com.example.antlr.hello.impl.base.FieldTypeEnum;
import com.example.antlr.hello.impl.functions.AbstractFunctionHandler;

/**
 * @author chijiuwang
 */
public class IsNullFunctionHandler extends AbstractFunctionHandler {

    private static final int ARG_SIZE = 1;

    @Override
    public HelloConvertListener.TreeNodeInfo processed() {
        if (this.expressionContexts.size() != ARG_SIZE) {
            throw new IllegalArgumentException("ISNULL(任意类型):不符合的参数要求");
        }
        String expr = String.format("cast((%s is null) as signed)", getParseTreeNode(this.expressionContexts.get(0)).getExpr());
        return generateTreeNode(expr, FieldTypeEnum.NUMBER);
    }
}
