package com.protectline.bpmninjs.xmlparser;

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