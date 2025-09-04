package com.protectline.bpmninjs.xmlparser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TokenSimplifierDebugTest {

    private TokenSimplifier tokenSimplifier;

    @BeforeEach
    public void setUp() {
        tokenSimplifier = new TokenSimplifier();
    }

    @Test
    public void debugMixedTags() {
        List<Token> tokens = List.of(
            // <parent>
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.STRING, "parent"),
            new Token(TOKEN_TYPE.CLOSE, ">"),
            // <selfClosing />
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.STRING, "selfClosing"),
            new Token(TOKEN_TYPE.END_SYMBOL, "/"),
            new Token(TOKEN_TYPE.CLOSE, ">"),
            // </parent>
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.END_SYMBOL, "/"),
            new Token(TOKEN_TYPE.STRING, "parent"),
            new Token(TOKEN_TYPE.CLOSE, ">")
        );

        System.out.println("Input tokens:");
        for (int i = 0; i < tokens.size(); i++) {
            System.out.println(i + ": " + tokens.get(i).getType() + " = " + tokens.get(i).getValue());
        }

        System.out.println("\nChecking patterns at different positions:");
        for (int i = 0; i < tokens.size(); i++) {
            System.out.println("Position " + i + ":");
            System.out.println("  isSelfCloseMark: " + SimplifiedTokenDefinition.isSelfCloseMark(tokens, i));
            System.out.println("  isCloseMark: " + SimplifiedTokenDefinition.isCloseMark(tokens, i));
            System.out.println("  isOpenMark: " + SimplifiedTokenDefinition.isOpenMark(tokens, i));
        }

        List<Token> result = tokenSimplifier.simplifyTokens(tokens);

        System.out.println("\nOutput tokens:");
        for (int i = 0; i < result.size(); i++) {
            System.out.println(i + ": " + result.get(i).getType() + " = " + result.get(i).getValue());
        }
    }
}