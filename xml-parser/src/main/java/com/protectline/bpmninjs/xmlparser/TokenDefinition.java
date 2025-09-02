package com.protectline.bpmninjs.xmlparser;

import java.util.Optional;

/**
 * Interface pour définir les patterns de tokens selon le type de contenu à parser
 */
public interface TokenDefinition {
    

    String getTypeValue(TOKEN_TYPE type);
    TOKEN_TYPE getType(String str);

    default String extractTokenString(String content, int position, int length) {
        return content.substring(position, position + length);
    }
}
