package com.example.antlr.hello.impl.functions.impl;

import com.example.antlr.hello.impl.base.FieldTypeEnum;
import com.example.antlr.hello.impl.functions.AbstractFunctionHandler;

import static com.example.antlr.hello.impl.HelloConvertListener.*;

/**
 * @author chijiuwang
 */
public class ReplaceFunctionHandler extends AbstractFunctionHandler {

    private static final int ARG_SIZE = 3;

    @Override
    public TreeNodeInfo processed() {
        if (this.expressionContexts.size() != ARG_SIZE) {
            throw new IllegalArgumentException("REPLACE:不符合的参数要求");
        }
        return generateTreeNode(
                String.format("REPLACE(\n" +
                        "    %s,\n" +
                        "    %s,\n" +
                        "    %s\n" +
                        ")",
                        convertToStringExpression(getParseTreeNode(this.expressionContexts.get(0))),
                        convertToStringExpression(getParseTreeNode(this.expressionContexts.get(1))),
                        convertToStringExpression(getParseTreeNode(this.expressionContexts.get(2)))
                ),
                FieldTypeEnum.STRING
        );
    }
}
