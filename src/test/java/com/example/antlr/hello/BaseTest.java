package com.example.antlr.hello;

import com.example.antlr.hello.impl.HelloConvertListener;
import com.example.antlr.hello.impl.HelloErrorListener;
import com.example.antlr.hello.impl.base.FieldTypeEnum;
import com.example.antlr.hello.impl.base.OperatorField;
import com.example.antlr.hello.impl.functions.FunctionFactory;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chijiuwang
 */
public class BaseTest {

    @Test
    public void test() {

        CharStream charStream = CharStreams.fromString("REPLACE(${金额},2=1,true)");

        HelloErrorListener helloErrorListener = new HelloErrorListener();

        HelloLexer helloLexer = new HelloLexer(charStream);
        helloLexer.removeErrorListeners();
        helloLexer.addErrorListener(helloErrorListener);
        CommonTokenStream commonTokenStream = new CommonTokenStream(helloLexer);

        HelloParser helloParser = new HelloParser(commonTokenStream);
        helloParser.removeErrorListeners();
        helloParser.addErrorListener(helloErrorListener);
        ParseTree parseTree = helloParser.expr();

        System.out.println(parseTree);
        System.out.println(parseTree.toStringTree());

        helloErrorListener.getSyntaxErrors().forEach(System.out::println);

        List<OperatorField> preFields = new ArrayList<>();
        preFields.add(OperatorField.builder().fieldId("金额").fieldType(FieldTypeEnum.NUMBER).build());
        preFields.add(OperatorField.builder().fieldId("流水").fieldType(FieldTypeEnum.NUMBER).build());
        preFields.add(OperatorField.builder().fieldId("开始时间").fieldType(FieldTypeEnum.DATE).build());
        preFields.add(OperatorField.builder().fieldId("大区").fieldType(FieldTypeEnum.STRING).build());

        ParseTreeWalker walker = new ParseTreeWalker();
        HelloConvertListener helloConvertListener = new HelloConvertListener(preFields);
        walker.walk(helloConvertListener, parseTree);

        System.out.println(helloConvertListener.getParseTreeProperty().get(parseTree).getRealExpr());
    }

    @Test
    public void baseTest() {
        System.out.println("yyyyy".replaceAll("yyyy", "%Y"));
    }

}
