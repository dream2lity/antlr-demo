package com.example.antlr.hello.impl.functions.impl;

import com.example.antlr.hello.impl.base.FieldTypeEnum;
import com.example.antlr.hello.impl.functions.AbstractFunctionHandler;

import static com.example.antlr.hello.impl.HelloConvertListener.*;

/**
 * @author chijiuwang
 */
public class RandBetweenFunctionHandler extends AbstractFunctionHandler {

    private static final int ARG_SIZE = 2;

    @Override
    public TreeNodeInfo processed() {
        if (this.expressionContexts.size() != ARG_SIZE) {
            throw new IllegalArgumentException("RANDBETWEEN(数值,数值):不符合的参数要求");
        }
        TreeNodeInfo min = getParseTreeNode(this.expressionContexts.get(0)),
                max = getParseTreeNode(this.expressionContexts.get(1));
        if (isNumberType(min.getType()) && isNumberType(max.getType())) {
            return generateTreeNode(
                    String.format("CEIL((FLOOR(%s) + (RAND() * (FLOOR(%s) - FLOOR(%s)))))", min.getRealExpr(), max.getRealExpr(), min.getRealExpr()),
                    FieldTypeEnum.NUMBER
            );
        } else {
            throw new IllegalArgumentException("RANDBETWEEN(数值,数值):不符合的参数要求");
        }
    }
}
