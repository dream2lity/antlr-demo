package com.example.antlr.hello.impl.functions.impl;

import com.example.antlr.hello.impl.base.FieldTypeEnum;
import com.example.antlr.hello.impl.functions.AbstractFunctionHandler;

import static com.example.antlr.hello.HelloParser.*;
import static com.example.antlr.hello.impl.HelloConvertListener.*;

/**
 * @author chijiuwang
 */
public class IfFunctionHandler extends AbstractFunctionHandler {

    private static final int ARG_SIZE = 3;

    @Override
    public TreeNodeInfo processed() {
        int argSize = this.expressionContexts.size();
        if (argSize != ARG_SIZE) {
            throw new IllegalArgumentException("IF(布尔/数值,参数,参数):不符合的参数要求");
        }
        TreeNodeInfo treeNodeInfo1 = getParseTreeNode(this.expressionContexts.get(0)),
                treeNodeInfo2 = getParseTreeNode(this.expressionContexts.get(1)),
                treeNodeInfo3 = getParseTreeNode(this.expressionContexts.get(2));
        if (treeNodeInfo1.getCtx() instanceof BooleanExpressionContext ||
                treeNodeInfo1.getCtx() instanceof BooleanContext ||
                treeNodeInfo1.getType() == FieldTypeEnum.NUMBER) {
            if (treeNodeInfo2.getType() != treeNodeInfo3.getType()) {
                throw new IllegalArgumentException("IF(布尔/数值,参数,参数):表达式的结果类型必须相同");
            }
            return generateTreeNode(String.format("IF(%s, %s, %s)", treeNodeInfo1.getExpr(), treeNodeInfo2.getRealExpr(), treeNodeInfo3.getRealExpr()), treeNodeInfo2.getType());
        } else {
            throw new RuntimeException("IF(布尔/数值,参数,参数):不符合的参数要求");
        }
    }
}
