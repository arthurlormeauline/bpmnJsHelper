package com.protectline.bpmninjs.xmlparser;

import java.util.ArrayList;
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
     * Pattern: [OPEN, STRING, (EQUALS, STRING)*, CLOSE]
     */
    public static OpenMarkResult parseOpenMark(List<Token> tokens, int startIndex) {
        String firstStringValue = tokens.get(startIndex + 1).getStringValue().trim();
        int i = startIndex + 2; // Skip OPEN and first STRING
        
        Map<String, String> attributes = new HashMap<>();
        String elementName;
        
        // Si c'est juste [OPEN, STRING, CLOSE], la STRING est l'elementName
        if (i < tokens.size() && tokens.get(i).getType() == TOKEN_TYPE.CLOSE) {
            elementName = firstStringValue;
        } else {
            // Il y a des attributs, donc splitter la première STRING pour récupérer l'elementName
            String[] firstStringParts = firstStringValue.split("\\s+");
            elementName = firstStringParts[0];
            
            // Collecter tous les noms d'attributs de la première STRING
            List<String> attributeNames = new ArrayList<>();
            for (int j = 1; j < firstStringParts.length; j++) {
                attributeNames.add(firstStringParts[j]);
            }
            
            int attributeIndex = 0;
            
            // Parser les séquences (EQUALS, STRING)
            while (i < tokens.size() && tokens.get(i).getType() != TOKEN_TYPE.CLOSE) {
                if (tokens.get(i).getType() == TOKEN_TYPE.EQUALS &&
                    i + 1 < tokens.size() &&
                    tokens.get(i + 1).getType() == TOKEN_TYPE.STRING) {
                    
                    String valueString = tokens.get(i + 1).getStringValue().trim();
                    
                    // Si la valeur contient des espaces après une valeur quotée, splitter
                    String actualValue = valueString;
                    if (valueString.startsWith("\"") && valueString.contains("\" ")) {
                        int endQuoteIndex = valueString.indexOf("\" ") + 1;
                        actualValue = valueString.substring(0, endQuoteIndex);
                        String nextAttrName = valueString.substring(endQuoteIndex + 1).trim();
                        
                        // Ajouter le prochain nom d'attribut à la liste
                        if (!nextAttrName.isEmpty()) {
                            attributeNames.add(nextAttrName);
                        }
                    }
                    
                    // Assigner l'attribut si on a un nom disponible
                    if (attributeIndex < attributeNames.size()) {
                        String attrName = attributeNames.get(attributeIndex);
                        attributes.put(attrName, actualValue);
                    }
                    
                    attributeIndex++;
                    i += 2; // Skip EQUALS and STRING
                } else {
                    i++;
                }
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
