package com.example.antlr.hello.impl.functions.impl;

import com.example.antlr.hello.impl.base.FieldTypeEnum;
import com.example.antlr.hello.impl.functions.AbstractFunctionHandler;

import static com.example.antlr.hello.impl.HelloConvertListener.*;

/**
 * @author chijiuwang
 */
public class PowerFunctionHandler extends AbstractFunctionHandler {

    private static final int ARG_SIZE = 2;

    @Override
    public TreeNodeInfo processed() {
        if (this.expressionContexts.size() != ARG_SIZE) {
            throw new IllegalArgumentException("POWER(数值,数值):不符合的参数要求");
        }
        TreeNodeInfo base = getParseTreeNode(this.expressionContexts.get(0)),
                power = getParseTreeNode(this.expressionContexts.get(1));
        if (base.getType() != FieldTypeEnum.NUMBER || power.getType() != FieldTypeEnum.NUMBER) {
            throw new IllegalArgumentException("POWER(数值,数值):不符合的参数要求");
        }
        String expr = String.format("POWER(%s, %s)", base.getRealExpr(), power.getRealExpr());
        return generateTreeNode(expr, FieldTypeEnum.NUMBER);
    }
}
