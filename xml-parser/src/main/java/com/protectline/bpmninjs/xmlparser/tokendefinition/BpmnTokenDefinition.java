package com.protectline.bpmninjs.xmlparser.tokendefinition;

import com.protectline.bpmninjs.xmlparser.TOKEN_TYPE;
import com.protectline.bpmninjs.xmlparser.TokenDefinition;

/**
 * Définition des tokens pour les fichiers BPMN XML (<element>)
 */
public class BpmnTokenDefinition implements TokenDefinition {
    
    @Override
    public boolean matches(String content, int position, TOKEN_TYPE tokenType) {
        if (position >= content.length()) {
            return false;
        }
        
        char current = content.charAt(position);
        
        switch (tokenType) {
            case OPEN:
                return current == '<';
                
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
            case CLOSE:
            case END_SYMBOL:
            case EQUALS:
                return 1; // Tous les tokens BPMN font 1 caractère
            default:
                return 0;
        }
    }
}
