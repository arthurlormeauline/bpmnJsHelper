package com.protectline.bpmninjs.xmlparser;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.protectline.bpmninjs.xmlparser.TOKEN_TYPE.*;

/**
 * Lexer générique qui utilise une TokenDefinition pour parser différents formats
 */
public class Lexer {

    private final TokenDefinition tokenDefinition;

    public Lexer(TokenDefinition tokenDefinition) {
        this.tokenDefinition = tokenDefinition;
    }
    
    public List<Token> tokenize(String content) {
        var genLexer = new GenericLexer();
        return genLexer.tokenize(content, List.of(OPEN,EQUALS,CLOSE,END_SYMBOL), tokenDefinition);
    }
}
