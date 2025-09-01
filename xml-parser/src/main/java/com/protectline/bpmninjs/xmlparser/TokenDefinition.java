package com.protectline.bpmninjs.xmlparser;

import java.util.Optional;

/**
 * Interface pour définir les patterns de tokens selon le type de contenu à parser
 */
public interface TokenDefinition {
    
    boolean matches(String content, int position, TOKEN_TYPE tokenType);

    String getTypeValue(TOKEN_TYPE type);
    TOKEN_TYPE getType(String str);

    int getTokenLength(TOKEN_TYPE tokenType);
    default String extractTokenString(String content, int position, int length) {
        return content.substring(position, position + length);
    }
}
