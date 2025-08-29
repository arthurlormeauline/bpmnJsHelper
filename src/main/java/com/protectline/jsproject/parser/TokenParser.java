package com.protectline.jsproject.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parser qui transforme une liste de tokens en éléments via une approche en deux étapes
 */
public class TokenParser {
    
    private final TokenSimplifier tokenSimplifier;
    
    public TokenParser() {
        this.tokenSimplifier = new TokenSimplifier();
    }
    
    /**
     * Parse une liste de tokens en éléments de façon récursive
     */
    public List<Element> parseTokensToElements(List<Token> tokens) {
        // Étape 1: Transformer les tokens de base en OPEN_MARK/CONTENT/CLOSE_MARK
        List<Token> simplifiedTokens = tokenSimplifier.simplifyTokens(tokens);
        
        // Étape 2: Parser récursivement les OPEN_MARK en éléments
        List<Element> elements = new ArrayList<>();
        parseElementsRecursively(simplifiedTokens, 0, elements);
        
        return elements;
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
}