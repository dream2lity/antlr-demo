package com.example.antlr.hello.impl.functions.impl;

import com.example.antlr.hello.impl.base.FieldTypeEnum;
import com.example.antlr.hello.impl.functions.AbstractFunctionHandler;

import static com.example.antlr.hello.impl.HelloConvertListener.*;

/**
 * @author chijiuwang
 */
public class TimeFunctionHandler extends AbstractFunctionHandler {

    private static final int ARG_SIZE = 3;

    @Override
    public TreeNodeInfo processed() {
        if (this.expressionContexts.size() != ARG_SIZE) {
            throw new IllegalArgumentException("TIME(数值,数值,数值):不符合的参数要求");
        }
        TreeNodeInfo hour = getParseTreeNode(this.expressionContexts.get(0)),
                minute = getParseTreeNode(this.expressionContexts.get(1)),
                second = getParseTreeNode(this.expressionContexts.get(2));
        if (isNumberType(hour.getType()) && isNumberType(minute.getType()) && isNumberType(second.getType())) {
            return generateTreeNode(
                    String.format("STR_TO_DATE(\n" +
                            "  CONCAT(\n" +
                            "    DATE_FORMAT(CURRENT_DATE(),'%%Y-%%m-%%d'),\n" +
                            "    ' ',\n" +
                            "    CAST(%s AS CHAR),\n" +
                            "    ':',\n" +
                            "    CAST(%s AS CHAR),\n" +
                            "    ':',\n" +
                            "    CAST(%s AS CHAR)\n" +
                            "  ),\n" +
                            "  '%%Y-%%m-%%d %%H:%%i:%%s'\n" +
                            ")", hour.getRealExpr(), minute.getRealExpr(), second.getRealExpr()),
                    FieldTypeEnum.DATETIME
            );
        } else {
            throw new IllegalArgumentException("TIME(数值,数值,数值):不符合的参数要求");
        }
    }
}
