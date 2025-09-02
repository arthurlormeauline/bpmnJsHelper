package com.protectline.bpmninjs.xmlparser.tokendefinition;

import com.protectline.bpmninjs.xmlparser.TOKEN_TYPE;
import com.protectline.bpmninjs.xmlparser.TokenDefinition;

import java.util.Optional;

import static com.protectline.bpmninjs.xmlparser.TOKEN_TYPE.*;

/**
 * Définition des tokens pour les fichiers BPMN XML (<element>)
 */
public class BpmnTokenDefinition implements TokenDefinition {


    @Override
    public String getTypeValue(TOKEN_TYPE type) {
        return switch (type) {
            case OPEN -> "<";
            case CLOSE -> ">";
            case EQUALS -> "=";
            case END_SYMBOL -> "/";
            default -> throw new IllegalArgumentException("No strign for this type : " + type);
        };
    }

    @Override
    public TOKEN_TYPE getType(String str) {
        if (str.equals("<")){
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


}
