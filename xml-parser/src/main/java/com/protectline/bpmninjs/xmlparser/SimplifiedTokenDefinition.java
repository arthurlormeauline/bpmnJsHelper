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
     * Pattern: [OPEN, STRING]
     */
    public static List<TOKEN_TYPE> getOpenMarkTokens() {
        return List.of(TOKEN_TYPE.OPEN, TOKEN_TYPE.STRING);
    }
    
    /**
     * Retourne la séquence de tokens qui compose un CLOSE_MARK
     * Pattern: [OPEN, END_SYMBOL, STRING, CLOSE]
     */
    public static List<TOKEN_TYPE> getCloseMarkTokens() {
        return List.of(TOKEN_TYPE.OPEN, TOKEN_TYPE.END_SYMBOL, TOKEN_TYPE.STRING, TOKEN_TYPE.CLOSE);
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
        // La STRING est à l'index 1 dans le pattern [OPEN, STRING]
        String stringValue = tokens.get(startIndex + 1).getStringValue();
        // Extraire l'elementName en splittant par des espaces et en prenant le premier élément
        String[] parts = stringValue.trim().split("\\s+");
        String elementName = parts[0];
        int i = startIndex + pattern.size();
        
        // Parser les attributs - d'abord extraire les noms d'attributs de la STRING initiale
        Map<String, String> attributes = new HashMap<>();
        
        // Si la STRING contient plus que juste l'elementName, extraire les noms d'attributs
        String[] allParts = stringValue.trim().split("\\s+");
        String pendingAttrName = null;
        if (allParts.length > 1) {
            // Il y a des noms d'attributs potentiels dans la STRING
            for (int partIndex = 1; partIndex < allParts.length; partIndex++) {
                pendingAttrName = allParts[partIndex];
                // Chercher = et valeur dans les tokens suivants
                if (i < tokens.size() && tokens.get(i).getType() == TOKEN_TYPE.EQUALS &&
                    i + 1 < tokens.size() && tokens.get(i + 1).getType() == TOKEN_TYPE.STRING) {
                    
                    String attrValue = tokens.get(i + 1).getStringValue().trim();
                    attributes.put(pendingAttrName, attrValue);
                    i += 2; // Skip = and value
                    pendingAttrName = null;
                }
            }
        }
        
        // Continuer à parser les attributs normaux jusqu'au >
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
        // La STRING est à l'index 2 dans le pattern [OPEN, END_SYMBOL, STRING, CLOSE]
        String stringValue = tokens.get(startIndex + 2).getStringValue();
        // Extraire l'elementName en splittant par des espaces et en prenant le premier élément
        String[] parts = stringValue.trim().split("\\s+");
        String elementName = parts[0];
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
