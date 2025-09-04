package com.protectline.bpmninjs.xmlparser.lexer.tokendefinition;

import com.protectline.bpmninjs.xmlparser.lexer.TOKEN_TYPE;
import com.protectline.bpmninjs.xmlparser.lexer.Token;
import com.protectline.bpmninjs.xmlparser.parser.CloseMark;
import com.protectline.bpmninjs.xmlparser.parser.OpenMark;
import com.protectline.bpmninjs.xmlparser.parser.SelfCloseMark;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
     * Retourne la séquence de tokens qui compose un SELF_CLOSE_MARK
     * Pattern: [OPEN, STRING, END_SYMBOL, CLOSE]
     */
    public static List<TOKEN_TYPE> getSelfCloseMarkTokens() {
        return List.of(TOKEN_TYPE.OPEN, TOKEN_TYPE.STRING, TOKEN_TYPE.END_SYMBOL, TOKEN_TYPE.CLOSE);
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
     * Vérifie si on a un pattern SELF_CLOSE_MARK à la position donnée
     * Supporte deux patterns :
     * 1. [OPEN, STRING, ..., END_SYMBOL, CLOSE] - balises classiques auto-fermantes
     * 2. [OPEN=<?, STRING, ..., CLOSE=?>] - déclarations XML
     */
    public static boolean isSelfCloseMark(List<Token> tokens, int index) {
        if (index + 3 >= tokens.size()) { // Minimum 4 tokens
            return false;
        }
        
        // Vérifier le début: OPEN, STRING
        if (tokens.get(index).getType() != TOKEN_TYPE.OPEN ||
            tokens.get(index + 1).getType() != TOKEN_TYPE.STRING) {
            return false;
        }
        
        // Pattern 1: Déclaration XML [<?...?>]
        if (isXmlDeclaration(tokens, index)) {
            return true;
        }
        
        // Pattern 2: Balise auto-fermante classique [<element ... />]
        // Chercher END_SYMBOL suivi de CLOSE, mais s'arrêter si on rencontre OPEN ou CLOSE avant
        for (int i = index + 2; i < tokens.size() - 1; i++) {
            TOKEN_TYPE currentType = tokens.get(i).getType();
            TOKEN_TYPE nextType = tokens.get(i + 1).getType();
            
            if (currentType == TOKEN_TYPE.END_SYMBOL && nextType == TOKEN_TYPE.CLOSE) {
                return true;
            } else if (currentType == TOKEN_TYPE.OPEN || currentType == TOKEN_TYPE.CLOSE) {
                // On a rencontré une autre balise avant de trouver END_SYMBOL
                return false;
            }
        }
        
        return false;
    }
    
    /**
     * Vérifie si on a une déclaration XML: [<? ... ?>]
     */
    private static boolean isXmlDeclaration(List<Token> tokens, int index) {
        if (index + 2 >= tokens.size()) {
            return false;
        }
        
        // Vérifier que ça commence par <?
        Token openToken = tokens.get(index);
        if (!openToken.getValue().equals("<?")) {
            return false;
        }
        
        // Chercher ?> sans rencontrer d'autres < ou >
        for (int i = index + 2; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getType() == TOKEN_TYPE.CLOSE && token.getValue().equals("?>")) {
                return true;
            } else if (token.getType() == TOKEN_TYPE.OPEN) {
                // Nouvelle balise avant la fin de la déclaration
                return false;
            }
        }
        
        return false;
    }
    
    /**
     * Parse un OPEN_MARK à partir de la position donnée
     * Pattern: [OPEN, STRING, (EQUALS, STRING)*, CLOSE]
     */
    public static OpenMarkResult parseOpenMark(List<Token> tokens, int startIndex) {
        ElementParsingResult result = parseElementWithAttributes(tokens, startIndex, TOKEN_TYPE.CLOSE);
        OpenMark openMark = new OpenMark(result.elementName, result.attributes);
        return new OpenMarkResult(openMark, result.nextIndex);
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
     * Parse un SELF_CLOSE_MARK à partir de la position donnée
     * Pattern: [OPEN, STRING, (EQUALS, STRING)*, END_SYMBOL, CLOSE] ou [<?xml, ..., ?>]
     */
    public static SelfCloseMarkResult parseSelfCloseMark(List<Token> tokens, int startIndex) {
        // Vérifier si c'est une déclaration XML
        if (isXmlDeclaration(tokens, startIndex)) {
            ElementParsingResult result = parseElementWithAttributes(tokens, startIndex, TOKEN_TYPE.CLOSE);
            SelfCloseMark selfCloseMark = new SelfCloseMark(result.elementName, result.attributes);
            return new SelfCloseMarkResult(selfCloseMark, result.nextIndex);
        } else {
            // Balise auto-fermante classique
            ElementParsingResult result = parseElementWithAttributes(tokens, startIndex, TOKEN_TYPE.END_SYMBOL);
            SelfCloseMark selfCloseMark = new SelfCloseMark(result.elementName, result.attributes);
            return new SelfCloseMarkResult(selfCloseMark, result.nextIndex);
        }
    }
    
    /**
     * Méthode commune pour parser un élément avec ses attributs
     * stopToken indique le token qui marque la fin du parsing (CLOSE pour OPEN_MARK, END_SYMBOL pour SELF_CLOSE_MARK)
     */
    private static ElementParsingResult parseElementWithAttributes(List<Token> tokens, int startIndex, TOKEN_TYPE stopToken) {
        String firstStringValue = tokens.get(startIndex + 1).getStringValue().trim();
        int i = startIndex + 2; // Skip OPEN and first STRING
        
        Map<String, String> attributes = new LinkedHashMap<>();
        String elementName;
        
        // Si c'est juste [OPEN, STRING, stopToken], la STRING est l'elementName
        if (i < tokens.size() && tokens.get(i).getType() == stopToken) {
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
            
            // Parser les séquences (EQUALS, value) et nouveaux attributs jusqu'au stopToken
            while (i < tokens.size() && tokens.get(i).getType() != stopToken) {
                if (tokens.get(i).getType() == TOKEN_TYPE.EQUALS) {
                    i++; // Skip EQUALS
                    
                    // Construire la valeur de l'attribut
                    StringBuilder valueBuilder = new StringBuilder();
                    
                    // Si on rencontre une QUOTE, collecter tout jusqu'à la QUOTE fermante
                    if (i < tokens.size() && tokens.get(i).getType() == TOKEN_TYPE.QUOTE) {
                        i++; // Skip opening quote (ne pas l'ajouter à la valeur)
                        
                        // Collecter tous les tokens jusqu'à la quote fermante
                        while (i < tokens.size() && tokens.get(i).getType() != TOKEN_TYPE.QUOTE) {
                            valueBuilder.append(tokens.get(i).getValue());
                            i++;
                        }
                        
                        // Skipper la quote fermante si présente (ne pas l'ajouter à la valeur)
                        if (i < tokens.size() && tokens.get(i).getType() == TOKEN_TYPE.QUOTE) {
                            i++; // Skip closing quote
                        }
                    } else if (i < tokens.size() && tokens.get(i).getType() == TOKEN_TYPE.STRING) {
                        // Valeur simple sans quotes
                        valueBuilder.append(tokens.get(i).getValue());
                        i++; // Skip STRING
                    }
                    
                    String actualValue = valueBuilder.toString();
                    
                    // Assigner l'attribut si on a un nom disponible
                    if (attributeIndex < attributeNames.size()) {
                        String attrName = attributeNames.get(attributeIndex);
                        attributes.put(attrName, actualValue);
                    }
                    
                    attributeIndex++;
                } else if (tokens.get(i).getType() == TOKEN_TYPE.STRING) {
                    // Nouveau nom d'attribut trouvé (ex: " xmlns:bpmndi")
                    String newAttrName = tokens.get(i).getValue().trim();
                    if (!newAttrName.isEmpty()) {
                        attributeNames.add(newAttrName);
                    }
                    i++;
                } else {
                    i++;
                }
            }
        }
        
        // Skip le stopToken
        if (i < tokens.size() && tokens.get(i).getType() == stopToken) {
            i++; // Skip stopToken (/ pour self-closing, ou juste avant > pour open)
        }
        
        // Pour les self-closing, il faut aussi skipper le CLOSE après le END_SYMBOL
        if (stopToken == TOKEN_TYPE.END_SYMBOL && i < tokens.size() && tokens.get(i).getType() == TOKEN_TYPE.CLOSE) {
            i++; // Skip >
        } else if (stopToken == TOKEN_TYPE.CLOSE) {
            // Pour les OPEN_MARK, on est déjà après le >
        }
        
        return new ElementParsingResult(elementName, attributes, i);
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
    
    /**
     * Classe pour encapsuler le résultat du parsing d'un SELF_CLOSE_MARK
     */
    @Getter
    public static class SelfCloseMarkResult {
        public final SelfCloseMark selfCloseMark;
        public final int nextIndex;
        
        public SelfCloseMarkResult(SelfCloseMark selfCloseMark, int nextIndex) {
            this.selfCloseMark = selfCloseMark;
            this.nextIndex = nextIndex;
        }
    }
    
    /**
     * Classe pour encapsuler le résultat du parsing commun d'un élément avec attributs
     */
    private static class ElementParsingResult {
        public final String elementName;
        public final Map<String, String> attributes;
        public final int nextIndex;
        
        public ElementParsingResult(String elementName, Map<String, String> attributes, int nextIndex) {
            this.elementName = elementName;
            this.attributes = attributes;
            this.nextIndex = nextIndex;
        }
    }
}
