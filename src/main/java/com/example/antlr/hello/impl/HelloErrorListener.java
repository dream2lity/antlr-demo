package com.example.antlr.hello.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.LinkedList;
import java.util.List;

/**
 * @author chijiuwang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HelloErrorListener extends BaseErrorListener {

    protected List<String> syntaxErrors = new LinkedList<>();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        syntaxErrors.add("line " + line + ":" + charPositionInLine + " " + msg);
    }
}
