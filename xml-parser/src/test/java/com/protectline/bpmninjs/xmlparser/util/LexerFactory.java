package com.protectline.bpmninjs.xmlparser.util;

import com.protectline.bpmninjs.xmlparser.lexer.GenericLexer;
import com.protectline.bpmninjs.xmlparser.lexer.Token;

import java.util.List;

import static com.protectline.bpmninjs.xmlparser.lexer.TOKEN_TYPE.*;

/**
 * Factory pour créer des lexers spécialisés
 */
public class LexerFactory {
    
    /**
     * Crée un lexer pour les projets JavaScript (//<element>)
     */
    public static GenericLexer createJsProjectLexer() {
        return new GenericLexer();
    }
    
    /**
     * Tokenise le contenu avec la définition JS Project
     */
    public static List<Token> tokenizeJsProject(String content) {
        GenericLexer lexer = new GenericLexer();
        return lexer.tokenize(content, List.of(CLOSE,OPEN,EQUALS,END_SYMBOL), new JsProjectTokenDefinition());
    }
    
    /**
     * Crée un lexer pour les fichiers BPMN XML (<element>)
     */
    public static GenericLexer createBpmnLexer() {
        return new GenericLexer();
    }
    
    /**
     * Tokenise le contenu avec la définition BPMN
     */
    public static List<Token> tokenizeBpmn(String content) {
        GenericLexer lexer = new GenericLexer();
        return lexer.tokenize(content, List.of(CLOSE,OPEN,EQUALS,END_SYMBOL), new BpmnTokenDefinition());
    }
}
