package com.example.antlr.hello.impl.functions.impl;

import com.example.antlr.hello.HelloParser;
import com.example.antlr.hello.impl.base.FieldTypeEnum;
import com.example.antlr.hello.impl.functions.AbstractFunctionHandler;

import java.util.StringJoiner;

import static com.example.antlr.hello.impl.HelloConvertListener.*;

/**
 * @author chijiuwang
 */
public class NvlFunctionHandler extends AbstractFunctionHandler {
    private static final int MIN_ARG_SIZE = 1;

    @Override
    public TreeNodeInfo processed() {
        if (this.expressionContexts.size() < MIN_ARG_SIZE) {
            throw new IllegalArgumentException("NVL(任意类型...):不符合的参数要求");
        }
        FieldTypeEnum finalType = getParseTreeNode(this.expressionContexts.get(0)).getType();
        TreeNodeInfo arg;
        StringJoiner joiner = new StringJoiner(",\n\t", "COALESCE(\n\t", "\n\t)");
        for (HelloParser.ExpressionContext expressionContext : this.expressionContexts) {
            arg = getParseTreeNode(expressionContext);
            if (arg.getType() != finalType) {
                throw new IllegalArgumentException("NVL:不符合所有参数为相同类型的要求");
            }
            joiner.add(arg.getRealExpr());
        }
        return generateTreeNode(joiner.toString(), finalType);
    }
}
