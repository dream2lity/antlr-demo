package com.example.antlr.hello.impl.functions.impl;

import com.example.antlr.hello.impl.HelloConvertListener;
import com.example.antlr.hello.impl.base.FieldTypeEnum;
import com.example.antlr.hello.impl.functions.AbstractFunctionHandler;

/**
 * @author chijiuwang
 */
public class NowFunctionHandler extends AbstractFunctionHandler {

    private static final int ARG_SIZE = 0;

    @Override
    public HelloConvertListener.TreeNodeInfo processed() {
        if (this.expressionContexts.size() != ARG_SIZE) {
            throw new IllegalArgumentException("NOW():不符合的参数要求");
        }
        return generateTreeNode("CURRENT_TIMESTAMP()", FieldTypeEnum.DATETIME);
    }
}
