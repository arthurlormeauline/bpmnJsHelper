package com.protectline.bpmninjs.xmlparser;

public enum TOKEN_TYPE {
    // Tokens de base (lexer)
    OPEN,         // < (anciennement OPEN)
    CLOSE,        // > (anciennement CLOSE)
    EQUALS,       // =
    END_SYMBOL,   // /
    ELEMENT,      // element name after <
    STRING,       // any string content
    
    // Tokens complexes (token parser - étape intermédiaire)
    OPEN_MARK,    // balise ouvrante avec attributs
    CONTENT,      // contenu sous forme de string
    CLOSE_MARK    // balise fermante
}
