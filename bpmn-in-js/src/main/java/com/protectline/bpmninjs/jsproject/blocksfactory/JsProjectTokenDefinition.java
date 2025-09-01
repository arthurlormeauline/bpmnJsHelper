package com.protectline.bpmninjs.jsproject.blocksfactory;

import com.protectline.bpmninjs.xmlparser.TOKEN_TYPE;
import com.protectline.bpmninjs.xmlparser.TokenDefinition;

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
                // Vérifie si on a "//<"
                return current == '/' && 
                       position + 2 < content.length() && 
                       content.charAt(position + 1) == '/' && 
                       content.charAt(position + 2) == '<';
                
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
