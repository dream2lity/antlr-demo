package com.example.antlr.hello.impl.functions.impl;

import com.example.antlr.hello.impl.base.FieldTypeEnum;
import com.example.antlr.hello.impl.functions.AbstractFunctionHandler;

import static com.example.antlr.hello.impl.HelloConvertListener.*;

/**
 * @author chijiuwang
 */
public class UpperFunctionHandler extends AbstractFunctionHandler {

    private static final int ARG_SIZE = 1;

    protected String functionRealName = "UPPER";

    @Override
    public TreeNodeInfo processed() {
        if (this.expressionContexts.size() != ARG_SIZE) {
            throw new IllegalArgumentException(functionRealName + "(文本):不符合的参数要求");
        }
        TreeNodeInfo text = getParseTreeNode(this.expressionContexts.get(0));
        if (isStringType(text.getType())) {
            return generateTreeNode(String.format(functionRealName + "(%s)", text.getExpr()), FieldTypeEnum.STRING);
        } else {
            throw new IllegalArgumentException(functionRealName + "(文本):不符合的参数要求");
        }
    }
}
