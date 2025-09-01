package com.protectline.bpmninjs.xmlparser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Définition des patterns de tokens complexes pour le TokenSimplifier
 */
public class SimplifiedTokenDefinition {
    
    /**
     * Retourne la séquence de tokens qui compose un OPEN_MARK
     * Pattern: [OPEN, ELEMENT]
     */
    public static List<TOKEN_TYPE> getOpenMarkTokens() {
        return List.of(TOKEN_TYPE.OPEN, TOKEN_TYPE.ELEMENT);
    }
    
    /**
     * Retourne la séquence de tokens qui compose un CLOSE_MARK
     * Pattern: [OPEN, END_SYMBOL, ELEMENT, CLOSE]
     */
    public static List<TOKEN_TYPE> getCloseMarkTokens() {
        return List.of(TOKEN_TYPE.OPEN, TOKEN_TYPE.END_SYMBOL, TOKEN_TYPE.ELEMENT, TOKEN_TYPE.CLOSE);
    }
    
    /**
     * Vérifie si on a un pattern OPEN_MARK à la position donnée
     */
    public static boolean isOpenMark(List<Token> tokens, int index) {
        List<TOKEN_TYPE> pattern = getOpenMarkTokens();
        if (index + pattern.size() - 1 >= tokens.size()) {
            return false;
        }
        
        for (int i = 0; i < pattern.size(); i++) {
            if (tokens.get(index + i).getType() != pattern.get(i)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Vérifie si on a un pattern CLOSE_MARK à la position donnée
     */
    public static boolean isCloseMark(List<Token> tokens, int index) {
        List<TOKEN_TYPE> pattern = getCloseMarkTokens();
        if (index + pattern.size() - 1 >= tokens.size()) {
            return false;
        }
        
        for (int i = 0; i < pattern.size(); i++) {
            if (tokens.get(index + i).getType() != pattern.get(i)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Parse un OPEN_MARK à partir de la position donnée
     */
    public static OpenMarkResult parseOpenMark(List<Token> tokens, int startIndex) {
        List<TOKEN_TYPE> pattern = getOpenMarkTokens();
        // L'ELEMENT est à l'index 1 dans le pattern [OPEN, ELEMENT]
        String elementName = tokens.get(startIndex + 1).getStringValue();
        int i = startIndex + pattern.size();
        
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
    public static CloseMarkResult parseCloseMark(List<Token> tokens, int startIndex) {
        List<TOKEN_TYPE> pattern = getCloseMarkTokens();
        // L'ELEMENT est à l'index 2 dans le pattern [OPEN, END_SYMBOL, ELEMENT, CLOSE]
        String elementName = tokens.get(startIndex + 2).getStringValue();
        int nextIndex = startIndex + pattern.size();
        
        CloseMark closeMark = new CloseMark(elementName);
        return new CloseMarkResult(closeMark, nextIndex);
    }
    
    /**
     * Classe pour encapsuler le résultat du parsing d'un OPEN_MARK
     */
    public static class OpenMarkResult {
        public final OpenMark openMark;
        public final int nextIndex;
        
        public OpenMarkResult(OpenMark openMark, int nextIndex) {
            this.openMark = openMark;
            this.nextIndex = nextIndex;
        }
    }
    
    /**
     * Classe pour encapsuler le résultat du parsing d'un CLOSE_MARK
     */
    public static class CloseMarkResult {
        public final CloseMark closeMark;
        public final int nextIndex;
        
        public CloseMarkResult(CloseMark closeMark, int nextIndex) {
            this.closeMark = closeMark;
            this.nextIndex = nextIndex;
        }
    }
}
