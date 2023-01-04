package com.example.antlr.hello.impl.functions.impl;

import com.example.antlr.hello.impl.base.FieldTypeEnum;
import com.example.antlr.hello.impl.functions.AbstractFunctionHandler;

import static com.example.antlr.hello.impl.HelloConvertListener.*;

/**
 * @author chijiuwang
 */
public class RoundFunctionHandler extends AbstractFunctionHandler {

    private static final int ARG_SIZE = 2;

    @Override
    public TreeNodeInfo processed() {
        if (this.expressionContexts.size() != ARG_SIZE) {
            throw new IllegalArgumentException("ROUND(数值,数值):不符合的参数要求");
        }
        TreeNodeInfo number = getParseTreeNode(this.expressionContexts.get(0)),
                digits = getParseTreeNode(this.expressionContexts.get(1));
        if (isNumberType(number.getType()) && isNumberType(digits.getType())) {
            return generateTreeNode(
                    String.format("ROUND(\n" +
                    "    %s,\n" +
                    "    %s\n" +
                    ")", number.getRealExpr(), digits.getRealExpr()),
                    FieldTypeEnum.NUMBER);
        } else {
            throw new IllegalArgumentException("ROUND(数值,数值):不符合的参数要求");
        }
    }
}
