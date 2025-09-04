package com.protectline.bpmninjs.model.jsproject;

import com.protectline.bpmninjs.xmlparser.lexer.TOKEN_TYPE;
import com.protectline.bpmninjs.xmlparser.lexer.TokenDefinition;

import java.util.List;

import static com.protectline.bpmninjs.xmlparser.lexer.TOKEN_TYPE.*;

/**
 * Définition des tokens pour les projets JavaScript (//<element>)
 */
public class JsProjectTokenDefinition implements TokenDefinition {
    
    @Override
    public List<String> getTypeValues(TOKEN_TYPE type) {
        return switch (type) {
            case OPEN -> List.of("//<");
            case CLOSE -> List.of(">");
            case EQUALS -> List.of("=");
            case END_SYMBOL -> List.of("/");
            case QUOTE -> List.of("\"");
            default -> throw new IllegalArgumentException("No strings for this type : " + type);
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
        }else if(str.equals("\"")){
            return QUOTE;
        }
        return STRING;
    }
}
