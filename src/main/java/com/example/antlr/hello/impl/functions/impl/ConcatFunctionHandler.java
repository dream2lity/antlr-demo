package com.example.antlr.hello.impl.functions.impl;

import com.example.antlr.hello.HelloParser;
import com.example.antlr.hello.impl.base.FieldTypeEnum;
import com.example.antlr.hello.impl.functions.AbstractFunctionHandler;

import java.util.StringJoiner;

import static com.example.antlr.hello.HelloParser.*;
import static com.example.antlr.hello.impl.HelloConvertListener.*;

/**
 *
 * concat(
 *             ' Monthly Report',
 *             '1',
 *             '2',
 *             cast((1 + 2) as char),
 *             case when 1 = 4 then '1'
 *                  when not(1 = 4) then '0'
 *                  else null
 *             end,
 *             '1',
 *             date_format(`T_694FCE`.`start_date`,'%Y-%m-%d'),
 *             cast(`T_694FCE`.`amount` as char)
 *           )
 *
 * @author chijiuwang
 */
public class ConcatFunctionHandler extends AbstractFunctionHandler {
    private static final int MIN_ARG_SIZE = 1;

    @Override
    public TreeNodeInfo processed() {
        if (this.expressionContexts.size() < MIN_ARG_SIZE) {
            throw new IllegalArgumentException("CONCAT(任意类型...):不符合的参数要求");
        }
        StringJoiner joiner = new StringJoiner(",\n\t", "CONCAT(\n\t", "\n)");
        TreeNodeInfo item;
        for (HelloParser.ExpressionContext expressionContext : this.expressionContexts) {
            item = getParseTreeNode(expressionContext);
            joiner.add(convertToStringExpression(item));
        }

        return generateTreeNode(joiner.toString(), FieldTypeEnum.STRING);
    }

}
