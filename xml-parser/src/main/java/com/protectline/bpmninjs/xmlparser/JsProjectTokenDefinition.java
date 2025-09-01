package com.protectline.bpmninjs.xmlparser;

import java.util.Optional;

import static com.protectline.bpmninjs.xmlparser.TOKEN_TYPE.*;

/**
 * Définition des tokens pour les projets JavaScript (//<element>)
 */
public class JsProjectTokenDefinition implements TokenDefinition {
    
    @Override
    public boolean matches(String content, int position, TOKEN_TYPE tokenType) {
        if (position >= content.length()) {
            return false;
        }
        
        char current = content.charAt(position);
        
        switch (tokenType) {
            case OPEN:
                // Vérifie si on a "//<" (ouverture normale) ou "//< / " (fermeture qui précède souvent une ouverture)
                if (current == '/' && position + 2 < content.length() && 
                    content.charAt(position + 1) == '/' && 
                    content.charAt(position + 2) == '<') {
                    // Si c'est une fermeture "//</", on ne considère pas ça comme OPEN
                    if (position + 3 < content.length() && content.charAt(position + 3) == '/') {
                        return false;
                    }
                    return true;
                }
                // Vérifie si on a "//</":
                if (current == '/' && position + 3 < content.length() && 
                    content.charAt(position + 1) == '/' && 
                    content.charAt(position + 2) == '<' &&
                    content.charAt(position + 3) == '/') {
                    return true;
                }
                return false;
                
            case CLOSE:
                return current == '>';
                
            case END_SYMBOL:
                return current == '/';
                
            case EQUALS:
                return current == '=';
                
            default:
                return false;
        }
    }

    @Override
    public String getTypeValue(TOKEN_TYPE type) {
        return switch (type) {
            case OPEN -> "//<";
            case CLOSE -> ">";
            case EQUALS -> "=";
            case END_SYMBOL -> "/";
            default -> throw new IllegalArgumentException("No strign for this type : " + type);
        };
    }

    @Override
    public TOKEN_TYPE getType(String str) {
        if (str.equals("//<")){
            return OPEN;
        }else if(str.equals(">")){
            return CLOSE;
        }else if(str.equals("=")){
            return EQUALS;
        }else if(str.equals("/")){
            return END_SYMBOL;
        }
        return STRING;
    }

    @Override
    public int getTokenLength(TOKEN_TYPE tokenType) {
        switch (tokenType) {
            case OPEN:
                return 3; // "//<"
            case CLOSE:
            case END_SYMBOL:
            case EQUALS:
                return 1;
            default:
                return 0;
        }
    }
}
