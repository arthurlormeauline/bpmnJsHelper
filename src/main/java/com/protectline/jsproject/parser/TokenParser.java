package com.protectline.jsproject.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parser qui transforme une liste de tokens en éléments via une approche en deux étapes
 */
public class TokenParser {
    
    /**
     * Parse une liste de tokens en éléments de façon récursive
     */
    public List<Element> parseTokensToElements(List<Token> tokens) {
        // Étape 1: Transformer les tokens de base en OPEN_MARK/CONTENT/CLOSE_MARK
        List<Token> simplifiedTokens = simplifyTokens(tokens);
        
        // Étape 2: Parser récursivement les OPEN_MARK en éléments
        List<Element> elements = new ArrayList<>();
        parseElementsRecursively(simplifiedTokens, 0, elements);
        
        return elements;
    }
    
    /**
     * Étape 1: Transforme [//][<][ELEMENT]...en [OPEN_MARK][CONTENT][CLOSE_MARK]
     */
    public List<Token> simplifyTokens(List<Token> tokens) {
        List<Token> result = new ArrayList<>();
        int i = 0;
        
        while (i < tokens.size()) {
            // Vérifier d'abord CLOSE_MARK car il est plus spécifique (a le / avant >)
            if (isCloseMarkPattern(tokens, i)) {
                // Chercher pattern CloseMark: [//][<][ELEMENT][/][>]
                CloseMarkResult closeMark = parseCloseMark(tokens, i);
                result.add(new Token(TOKEN_TYPE.CLOSE_MARK, closeMark.closeMark));
                i = closeMark.nextIndex;
                
            } else if (isOpenMarkPattern(tokens, i)) {
                // Chercher pattern OpenMark: [//][<][ELEMENT]([STRING][=][STRING])*[>]
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
     * Étape 2: Parse récursivement les éléments
     * À chaque OPEN_MARK on rentre dans la récursion, on continue jusqu'au CLOSE_MARK
     */
    private int parseElementsRecursively(List<Token> tokens, int startIndex, List<Element> elements) {
        int i = startIndex;
        
        while (i < tokens.size()) {
            if (tokens.get(i).getType() == TOKEN_TYPE.OPEN_MARK) {
                OpenMark openMark = tokens.get(i).getOpenMark();
                i++; // Avancer après l'OPEN_MARK
                
                // Collecter le contenu et les éléments enfants
                StringBuilder contentBuilder = new StringBuilder();
                List<Element> childElements = new ArrayList<>();
                
                while (i < tokens.size() && tokens.get(i).getType() != TOKEN_TYPE.CLOSE_MARK) {
                    if (tokens.get(i).getType() == TOKEN_TYPE.CONTENT) {
                        contentBuilder.append(tokens.get(i).getStringValue());
                        i++;
                    } else if (tokens.get(i).getType() == TOKEN_TYPE.OPEN_MARK) {
                        // Appel récursif sur l'élément enfant
                        i = parseElementsRecursively(tokens, i, childElements);
                    } else {
                        i++;
                    }
                }
                
                // Vérifier qu'on a bien un CLOSE_MARK correspondant
                if (i >= tokens.size() || tokens.get(i).getType() != TOKEN_TYPE.CLOSE_MARK) {
                    throw new IllegalStateException("Missing CLOSE_MARK for element: " + openMark.getElementName());
                }
                
                CloseMark closeMark = tokens.get(i).getCloseMark();
                if (!openMark.getElementName().equals(closeMark.getElementName())) {
                    throw new IllegalStateException("Mismatched tags: " + openMark.getElementName() + " != " + closeMark.getElementName());
                }
                
                // Créer l'élément avec le contenu final
                String content = contentBuilder.toString();
                Element element = new Element(openMark.getElementName(), openMark.getAttributes(), content);
                elements.add(element);
                
                i++; // Avancer après le CLOSE_MARK
                return i; // Retourner à la boucle parente
                
            } else {
                i++;
            }
        }
        
        return i;
    }
    
    private boolean isOpenMarkPattern(List<Token> tokens, int index) {
        return index + 2 < tokens.size() &&
               tokens.get(index).getType() == TOKEN_TYPE.SLASH_SLASH &&
               tokens.get(index + 1).getType() == TOKEN_TYPE.OPEN &&
               tokens.get(index + 2).getType() == TOKEN_TYPE.ELEMENT;
    }
    
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
    
    private CloseMarkResult parseCloseMark(List<Token> tokens, int startIndex) {
        String elementName = tokens.get(startIndex + 2).getStringValue();
        int nextIndex = startIndex + 5; // Skip [//][<][ELEMENT][/][>]
        
        CloseMark closeMark = new CloseMark(elementName);
        return new CloseMarkResult(closeMark, nextIndex);
    }
    
    private static class OpenMarkResult {
        final OpenMark openMark;
        final int nextIndex;
        
        OpenMarkResult(OpenMark openMark, int nextIndex) {
            this.openMark = openMark;
            this.nextIndex = nextIndex;
        }
    }
    
    private static class CloseMarkResult {
        final CloseMark closeMark;
        final int nextIndex;
        
        CloseMarkResult(CloseMark closeMark, int nextIndex) {
            this.closeMark = closeMark;
            this.nextIndex = nextIndex;
        }
    }
}