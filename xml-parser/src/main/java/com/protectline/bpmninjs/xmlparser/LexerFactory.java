package com.protectline.bpmninjs.xmlparser;

import com.protectline.bpmninjs.xmlparser.tokendefinition.BpmnTokenDefinition;

/**
 * Factory pour créer des lexers spécialisés
 */
public class LexerFactory {
    
    /**
     * Crée un lexer pour les projets JavaScript (//<element>)
     */
    public static Lexer createJsProjectLexer() {
        return new Lexer(new JsProjectTokenDefinition());
    }
    
    /**
     * Crée un lexer pour les fichiers BPMN XML (<element>)
     */
    public static Lexer createBpmnLexer() {
        return new Lexer(new BpmnTokenDefinition());
    }
}