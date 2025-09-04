package com.protectline.bpmninjs.xmlparser.lexer;

public enum TOKEN_TYPE {
    // Tokens de base (lexer)
    OPEN,         // <
    CLOSE,        // >
    EQUALS,       // =
    END_SYMBOL,   // /
    QUOTE,        // "
    ELEMENT,      //
    STRING,       //
    
    // Nouveaux tokens pour la tokenisation en deux étapes
    WORD,         // séquence de caractères non-blancs
    BLANK,        // séquence d'espaces, tabs, newlines
    
    // Tokens complexes (token parser - étape intermédiaire)
    OPEN_MARK,      // balise ouvrante avec attributs
    CONTENT,        // contenu sous forme de string
    CLOSE_MARK,     // balise fermante
    SELF_CLOSE_MARK // balise auto-fermante avec attributs
}
