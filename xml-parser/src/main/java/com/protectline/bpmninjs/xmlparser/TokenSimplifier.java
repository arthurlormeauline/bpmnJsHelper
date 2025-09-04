package com.protectline.bpmninjs.xmlparser;

import java.util.ArrayList;
import java.util.List;


public class TokenSimplifier {
    
    public List<Token> simplifyTokens(List<Token> tokens) {
        List<Token> result = new ArrayList<>();
        int i = 0;
        
        while (i < tokens.size()) {
            // Vérifier d'abord SELF_CLOSE_MARK car il est plus spécifique
            if (SimplifiedTokenDefinition.isSelfCloseMark(tokens, i)) {
                SimplifiedTokenDefinition.SelfCloseMarkResult selfCloseMark = SimplifiedTokenDefinition.parseSelfCloseMark(tokens, i);
                result.add(new Token(TOKEN_TYPE.SELF_CLOSE_MARK, selfCloseMark.selfCloseMark));
                i = selfCloseMark.nextIndex;
                
            // Puis vérifier CLOSE_MARK car il est plus spécifique que OPEN_MARK (a le / avant >)
            } else if (SimplifiedTokenDefinition.isCloseMark(tokens, i)) {
                SimplifiedTokenDefinition.CloseMarkResult closeMark = SimplifiedTokenDefinition.parseCloseMark(tokens, i);
                result.add(new Token(TOKEN_TYPE.CLOSE_MARK, closeMark.closeMark));
                i = closeMark.nextIndex;
                
            } else if (SimplifiedTokenDefinition.isOpenMark(tokens, i)) {
                SimplifiedTokenDefinition.OpenMarkResult openMark = SimplifiedTokenDefinition.parseOpenMark(tokens, i);
                result.add(new Token(TOKEN_TYPE.OPEN_MARK, openMark.openMark));
                i = openMark.nextIndex;
                
            } else {
                // Collecter les tokens restants comme CONTENT
                StringBuilder content = new StringBuilder();
                while (i < tokens.size() && 
                       !SimplifiedTokenDefinition.isSelfCloseMark(tokens, i) &&
                       !SimplifiedTokenDefinition.isOpenMark(tokens, i) && 
                       !SimplifiedTokenDefinition.isCloseMark(tokens, i)) {
                    content.append(tokens.get(i).getValue());
                    i++;
                }
                if (content.length() > 0) {
                    result.add(new Token(TOKEN_TYPE.CONTENT, content.toString()));
                }
            }
        }
        
        return result;
    }
    
}
