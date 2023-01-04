package com.example.antlr.hello.impl;

import com.example.antlr.hello.HelloBaseListener;
import com.example.antlr.hello.impl.base.FieldTypeEnum;
import com.example.antlr.hello.impl.base.OperatorField;
import com.example.antlr.hello.impl.functions.FunctionFactory;
import com.example.antlr.hello.impl.functions.AbstractFunctionHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.antlr.hello.HelloParser.*;

/**
 * @author chijiuwang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HelloConvertListener extends HelloBaseListener {
    ParseTreeProperty<TreeNodeInfo> parseTreeProperty = new ParseTreeProperty<>();
    Map<String, FieldTypeEnum> preFieldTypeMaps;

    public HelloConvertListener(List<OperatorField> preFields) {
        this.preFieldTypeMaps = Objects.nonNull(preFields) && !preFields.isEmpty() ?
                preFields.stream().collect(Collectors.toMap(OperatorField::getFieldId, OperatorField::getFieldType)) :
                Collections.emptyMap();
    }

    @Override
    public void exitNumber(NumberContext ctx) {
        this.parseTreeProperty.put(ctx, new TreeNodeInfo(ctx.getText(), ctx, FieldTypeEnum.NUMBER));
    }

    @Override
    public void exitBoolean(BooleanContext ctx) {
        this.parseTreeProperty.put(ctx, new TreeNodeInfo(ctx.getText(), ctx, FieldTypeEnum.NUMBER));
    }

    @Override
    public void exitString(StringContext ctx) {
        this.parseTreeProperty.put(ctx, new TreeNodeInfo(ctx.getText().replaceAll("\"", "'"), ctx, FieldTypeEnum.STRING));
    }

    @Override
    public void exitIdentifier(IdentifierContext ctx) {
        String ctxText = ctx.getText();
        String identifierName = ctxText.substring(2, ctxText.length() - 1);
        // TODO: 转换为当前字段名称
        if (!this.preFieldTypeMaps.containsKey(identifierName)) {
            throw new IllegalArgumentException("field [" + identifierName + "] was not found.");
        }
        this.parseTreeProperty.put(ctx, new TreeNodeInfo(identifierName, ctx, preFieldTypeMaps.get(identifierName)));
    }

    @Override
    public void exitBooleanExpression(BooleanExpressionContext ctx) {
        String expr1 = this.parseTreeProperty.get(ctx.expression(0)).getExpr();
        String expr2 = this.parseTreeProperty.get(ctx.expression(1)).getExpr();
        String finalExpr;
        switch (ctx.op.getType()) {
            case Equal:
                finalExpr = String.format("%s = %s", expr1, expr2);
                break;
            case NotEqual:
                finalExpr = String.format("%s != %s", expr1, expr2);
                break;
            case Less:
                finalExpr = String.format("%s < %s", expr1, expr2);
                break;
            case LessEqual:
                finalExpr = String.format("%s <= %s", expr1, expr2);
                break;
            case Greater:
                finalExpr = String.format("%s > %s", expr1, expr2);
                break;
            case GreaterEqual:
                finalExpr = String.format("%s >= %s", expr1, expr2);
                break;
            default:
                throw new RuntimeException("unsupported operator.");
        }
        this.parseTreeProperty.put(ctx, new TreeNodeInfo(finalExpr, ctx, FieldTypeEnum.NUMBER));
    }

    @Override
    public void exitMultiplicativeExpression(MultiplicativeExpressionContext ctx) {
        String expr1 = this.parseTreeProperty.get(ctx.expression(0)).getExpr();
        String expr2 = this.parseTreeProperty.get(ctx.expression(1)).getExpr();
        String finalExpr;
        switch (ctx.op.getType()) {
            case MUL:
                finalExpr = String.format("%s * %s", expr1, expr2);
                break;
            case DIV:
                finalExpr = String.format("%s / %s", expr1, expr2);
                break;
            default:
                throw new RuntimeException("unsupported operator.");
        }
        this.parseTreeProperty.put(ctx, new TreeNodeInfo(finalExpr, ctx, FieldTypeEnum.NUMBER));
    }

    @Override
    public void exitAdditiveExpression(AdditiveExpressionContext ctx) {
        String expr1 = this.parseTreeProperty.get(ctx.expression(0)).getExpr();
        String expr2 = this.parseTreeProperty.get(ctx.expression(1)).getExpr();
        String finalExpr;
        switch (ctx.op.getType()) {
            case ADD:
                finalExpr = String.format("%s + %s", expr1, expr2);
                break;
            case SUB:
                finalExpr = String.format("%s - %s", expr1, expr2);
                break;
            default:
                throw new RuntimeException("unsupported operator.");
        }
        this.parseTreeProperty.put(ctx, new TreeNodeInfo(finalExpr, ctx, FieldTypeEnum.NUMBER));
    }

    @Override
    public void exitParenExpression(ParenExpressionContext ctx) {
        String expr = ctx.expression() instanceof ParenExpressionContext ?
                String.format("%s", this.parseTreeProperty.get(ctx.expression()).getExpr()) :
                String.format("(%s)", this.parseTreeProperty.get(ctx.expression()).getExpr());
        this.parseTreeProperty.put(ctx,
                new TreeNodeInfo(expr, ctx,
                        this.parseTreeProperty.get(ctx.expression()).getType()));
    }

    @Override
    public void exitFunctionExpression(FunctionExpressionContext ctx) {
        try {
            AbstractFunctionHandler functionHandler = FunctionFactory.newInstance(ctx, this.parseTreeProperty::get);
            this.parseTreeProperty.put(ctx, functionHandler.processed());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void exitExpr(ExprContext ctx) {
        this.parseTreeProperty.put(ctx, this.parseTreeProperty.get(ctx.expression()));
    }

    @AllArgsConstructor
    @Data
    public static class TreeNodeInfo {
        String expr;
        ExpressionContext ctx;
        FieldTypeEnum type;

        public String getRealExpr() {
            if (this.ctx instanceof BooleanContext ||
                    this.ctx instanceof BooleanExpressionContext) {
                return String.format("cast((%s) as signed)", this.expr);
            } else if (this.ctx instanceof ParenExpressionContext) {
                ExpressionContext expressionContext = ((ParenExpressionContext) this.ctx).expression();
                if (expressionContext instanceof BooleanContext || expressionContext instanceof BooleanExpressionContext) {
                    return String.format("cast(%s as signed)", this.expr);
                }
            }
            return this.expr;
        }

        public boolean isSimpleBoolean() {
            return this.ctx instanceof ParenExpressionContext
                    ? ((ParenExpressionContext) this.ctx).expression() instanceof BooleanContext || ((ParenExpressionContext) this.ctx).expression() instanceof BooleanExpressionContext
                    : this.ctx instanceof BooleanContext || this.ctx instanceof BooleanExpressionContext;
        }

        public boolean isSimpleString() {
            return this.ctx instanceof ParenExpressionContext
                    ? ((ParenExpressionContext) this.ctx).expression() instanceof StringContext
                    : this.ctx instanceof StringContext;
        }

        public boolean isSimpleNumber() {
            return this.ctx instanceof ParenExpressionContext
                    ? ((ParenExpressionContext) this.ctx).expression() instanceof NumberContext
                    : this.ctx instanceof NumberContext;
        }
    }
}
