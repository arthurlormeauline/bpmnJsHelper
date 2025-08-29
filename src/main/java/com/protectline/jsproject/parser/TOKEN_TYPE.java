package com.protectline.jsproject.parser;

public enum TOKEN_TYPE {
    // Tokens de base (lexer)
    SLASH_SLASH,  // //
    OPEN,         // < (anciennement OPEN)
    CLOSE,        // > (anciennement CLOSE)
    EQUALS,       // =
    SLASH,        // /
    ELEMENT,      // element name after <
    STRING,       // any string content
    
    // Tokens complexes (token parser - étape intermédiaire)
    OPEN_MARK,    // balise ouvrante avec attributs
    CONTENT,      // contenu sous forme de string
    CLOSE_MARK    // balise fermante
}
