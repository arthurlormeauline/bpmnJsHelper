package com.protectline.bpmninjs.jsproject;

import com.protectline.bpmninjs.xmlparser.TOKEN_TYPE;
import com.protectline.bpmninjs.xmlparser.TokenDefinition;

public class JsProjectTokenDefinition implements TokenDefinition {
    
    @Override
    public String getTypeValue(TOKEN_TYPE type) {
        return switch (type) {
            case OPEN -> "//<";
            case CLOSE -> ">";
            case EQUALS -> "=";
            case END_SYMBOL -> "/";
            default -> throw new IllegalArgumentException("No string for this type: " + type);
        };
    }

    @Override
    public TOKEN_TYPE getType(String str) {
        if (str.equals("//<")){
            return TOKEN_TYPE.OPEN;
        }else if(str.equals(">")){
            return TOKEN_TYPE.CLOSE;
        }else if(str.equals("=")){
            return TOKEN_TYPE.EQUALS;
        }else if(str.equals("/")){
            return TOKEN_TYPE.END_SYMBOL;
        }
        return TOKEN_TYPE.STRING;
    }
}
