package com.example.antlr.hello.impl.functions.impl;

import com.example.antlr.hello.impl.base.FieldTypeEnum;
import com.example.antlr.hello.impl.functions.AbstractFunctionHandler;

import static com.example.antlr.hello.impl.HelloConvertListener.*;

/**
 * @author chijiuwang
 */
public class DateDelayFunctionHandler extends AbstractFunctionHandler {

    private static final int ARG_SIZE = 2;

    @Override
    public TreeNodeInfo processed() {
        if (this.expressionContexts.size() != ARG_SIZE) {
            throw new IllegalArgumentException("DATEDELAY(日期/文本,数值):不符合的参数要求");
        }
        TreeNodeInfo date = getParseTreeNode(this.expressionContexts.get(0)),
                interval = getParseTreeNode(this.expressionContexts.get(1));
        if (isDateOrStringType(date.getType()) && isNumberType(interval.getType())) {
            String expr = String.format("DATE_ADD(%s, INTERVAL %s DAY)", getFinalDate(date), interval.getRealExpr());
            return generateTreeNode(expr, FieldTypeEnum.DATETIME);
        } else {
            throw new IllegalArgumentException("DATEDELAY(日期/文本,数值):不符合的参数要求");
        }
    }
}
