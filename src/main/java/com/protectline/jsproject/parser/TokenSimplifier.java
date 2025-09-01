package com.protectline.jsproject.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class TokenSimplifier {
    
    List<Token> simplifyTokens(List<Token> tokens) {
        List<Token> result = new ArrayList<>();
        int i = 0;
        
        while (i < tokens.size()) {
            // Vérifier d'abord CLOSE_MARK car il est plus spécifique (a le / avant >)
            if (isCloseMarkPattern(tokens, i)) {
                CloseMarkResult closeMark = parseCloseMark(tokens, i);
                result.add(new Token(TOKEN_TYPE.CLOSE_MARK, closeMark.closeMark));
                i = closeMark.nextIndex;
                
            } else if (isOpenMarkPattern(tokens, i)) {
                OpenMarkResult openMark = parseOpenMark(tokens, i);
                result.add(new Token(TOKEN_TYPE.OPEN_MARK, openMark.openMark));
                i = openMark.nextIndex;
                
            } else {
                // Collecter les tokens restants comme CONTENT
                StringBuilder content = new StringBuilder();
                while (i < tokens.size() && !isOpenMarkPattern(tokens, i) && !isCloseMarkPattern(tokens, i)) {
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
    
    /**
     * Vérifie si on a un pattern OPEN_MARK à la position donnée
     * Pattern: [//][<][ELEMENT] (puis potentiellement des attributs) [>]
     */
    private boolean isOpenMarkPattern(List<Token> tokens, int index) {
        return index + 2 < tokens.size() &&
               tokens.get(index).getType() == TOKEN_TYPE.SLASH_SLASH &&
               tokens.get(index + 1).getType() == TOKEN_TYPE.OPEN &&
               tokens.get(index + 2).getType() == TOKEN_TYPE.ELEMENT;
    }
    
    /**
     * Vérifie si on a un pattern CLOSE_MARK à la position donnée
     * Pattern: [//][<][ELEMENT][/][>]
     */
    private boolean isCloseMarkPattern(List<Token> tokens, int index) {
        // Nous devons pouvoir accéder à index+4, donc index+4 < tokens.size()
        if (index + 4 >= tokens.size()) {
            return false;
        }
        return tokens.get(index).getType() == TOKEN_TYPE.SLASH_SLASH &&
               tokens.get(index + 1).getType() == TOKEN_TYPE.OPEN &&
               tokens.get(index + 2).getType() == TOKEN_TYPE.ELEMENT &&
               tokens.get(index + 3).getType() == TOKEN_TYPE.SLASH &&
               tokens.get(index + 4).getType() == TOKEN_TYPE.CLOSE;
    }
    
    /**
     * Parse un OPEN_MARK à partir de la position donnée
     */
    private OpenMarkResult parseOpenMark(List<Token> tokens, int startIndex) {
        String elementName = tokens.get(startIndex + 2).getStringValue();
        int i = startIndex + 3;
        
        // Parser les attributs jusqu'au >
        Map<String, String> attributes = new HashMap<>();
        while (i < tokens.size() && tokens.get(i).getType() != TOKEN_TYPE.CLOSE) {
            if (tokens.get(i).getType() == TOKEN_TYPE.STRING &&
                i + 2 < tokens.size() &&
                tokens.get(i + 1).getType() == TOKEN_TYPE.EQUALS &&
                tokens.get(i + 2).getType() == TOKEN_TYPE.STRING) {
                
                String attrName = tokens.get(i).getStringValue().trim();
                String attrValue = tokens.get(i + 2).getStringValue().trim();
                attributes.put(attrName, attrValue);
                i += 3;
            } else {
                i++;
            }
        }
        
        if (i < tokens.size() && tokens.get(i).getType() == TOKEN_TYPE.CLOSE) {
            i++; // Skip >
        }
        
        OpenMark openMark = new OpenMark(elementName, attributes);
        return new OpenMarkResult(openMark, i);
    }
    
    /**
     * Parse un CLOSE_MARK à partir de la position donnée
     */
    private CloseMarkResult parseCloseMark(List<Token> tokens, int startIndex) {
        String elementName = tokens.get(startIndex + 2).getStringValue();
        int nextIndex = startIndex + 5; // Skip [//][<][ELEMENT][/][>]
        
        CloseMark closeMark = new CloseMark(elementName);
        return new CloseMarkResult(closeMark, nextIndex);
    }
    
    /**
     * Classe pour encapsuler le résultat du parsing d'un OPEN_MARK
     */
    private static class OpenMarkResult {
        final OpenMark openMark;
        final int nextIndex;
        
        OpenMarkResult(OpenMark openMark, int nextIndex) {
            this.openMark = openMark;
            this.nextIndex = nextIndex;
        }
    }
    
    /**
     * Classe pour encapsuler le résultat du parsing d'un CLOSE_MARK
     */
    private static class CloseMarkResult {
        final CloseMark closeMark;
        final int nextIndex;
        
        CloseMarkResult(CloseMark closeMark, int nextIndex) {
            this.closeMark = closeMark;
            this.nextIndex = nextIndex;
        }
    }
}
