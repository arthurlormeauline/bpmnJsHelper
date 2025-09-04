package com.protectline.bpmninjs.xmlparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TokenParser {

    private final TokenSimplifier tokenSimplifier;

    public TokenParser() {
        this.tokenSimplifier = new TokenSimplifier();
    }

    public List<Element> parseXmlAndGetElements(List<Token> tokens) {
        // Pour la compatibilité descendante, utiliser l'artificial root mais 
        // ne retourner que ses enfants, pas l'artificial root lui-même
        Element artificialRoot = parseTokensToArtificialRoot(tokens);
        List<Element> allElements = new ArrayList<>();
        
        // Collecter tous les éléments des enfants, mais pas l'artificial root
        for (Element child : artificialRoot.getChildren()) {
            collectAllElements(child, allElements);
        }
        
        return allElements;
    }

    public Element parseXml(List<Token> tokens) {
        Element artificialRoot = parseTokensToArtificialRoot(tokens);
        
        // Trouver le premier élément non-déclaration XML comme document root
        Element documentRoot = null;
        for (Element child : artificialRoot.getChildren()) {
            if (!child.getElementName().equals("xml")) { // Skip XML declarations
                documentRoot = child;
                break;
            }
        }
        
        // Return the document root if we found one, otherwise return the artificial root
        return documentRoot != null ? documentRoot : artificialRoot;
    }
    
    private Element parseTokensToArtificialRoot(List<Token> tokens) {
        // Vérifier si les tokens sont déjà simplifiés (pour la compatibilité avec les tests)
        boolean alreadySimplified = tokens.stream()
                .anyMatch(token -> token.getType() == TOKEN_TYPE.OPEN_MARK || 
                                 token.getType() == TOKEN_TYPE.CLOSE_MARK ||
                                 token.getType() == TOKEN_TYPE.SELF_CLOSE_MARK);
        
        List<Token> simplifiedTokens = alreadySimplified ? tokens : tokenSimplifier.simplifyTokens(tokens);
        
        // Créer un élément root fictif qui contiendra tous les éléments top-level
        Element rootElement = new Element("", new HashMap<>(), new ArrayList<>());
        
        int i = 0;
        while (i < simplifiedTokens.size()) {
            TOKEN_TYPE currentType = simplifiedTokens.get(i).getType();
            
            if (currentType == TOKEN_TYPE.OPEN_MARK) {
                ParseResult result = parseElement(simplifiedTokens, i);
                rootElement.addChild(result.element);
                i = result.nextIndex;
                
            } else if (currentType == TOKEN_TYPE.SELF_CLOSE_MARK) {
                SelfCloseMark selfCloseMark = simplifiedTokens.get(i).getSelfCloseMark();
                Element selfClosingElement = new Element(selfCloseMark.getElementName(), 
                                                       selfCloseMark.getAttributes(), true);
                rootElement.addChild(selfClosingElement);
                i++;
                
            } else if (currentType == TOKEN_TYPE.CONTENT) {
                // Ignorer le contenu au niveau root (par exemple, les espaces entre éléments)
                i++;
                
            } else {
                i++;
            }
        }
        
        return rootElement;
    }
    
    private boolean isXmlDeclaration(SelfCloseMark selfCloseMark) {
        return "xml".equals(selfCloseMark.getElementName());
    }

    private ParseResult parseElement(List<Token> tokens, int startIndex) {
        if (tokens.get(startIndex).getType() != TOKEN_TYPE.OPEN_MARK) {
            throw new IllegalStateException("Expected OPEN_MARK at position " + startIndex);
        }

        OpenMark openMark = tokens.get(startIndex).getOpenMark();
        int i = startIndex + 1; // Avancer après l'OPEN_MARK

        List<Element> children = new ArrayList<>();
        StringBuilder contentBuilder = new StringBuilder();

        // Parser le contenu jusqu'au CLOSE_MARK correspondant
        while (i < tokens.size()) {
            TOKEN_TYPE currentType = tokens.get(i).getType();

            if (currentType == TOKEN_TYPE.CONTENT) {
                contentBuilder.append(tokens.get(i).getStringValue());
                i++;

            } else if (currentType == TOKEN_TYPE.OPEN_MARK) {
                ParseResult childResult = parseElement(tokens, i);
                children.add(childResult.element);
                i = childResult.nextIndex;

            } else if (currentType == TOKEN_TYPE.SELF_CLOSE_MARK) {
                SelfCloseMark selfCloseMark = tokens.get(i).getSelfCloseMark();
                Element selfClosingChild = new Element(selfCloseMark.getElementName(), 
                                                     selfCloseMark.getAttributes(), true);
                children.add(selfClosingChild);
                i++;

            } else if (currentType == TOKEN_TYPE.CLOSE_MARK) {
                CloseMark closeMark = tokens.get(i).getCloseMark();

                if (!openMark.getElementName().equals(closeMark.getElementName())) {
                    throw new IllegalStateException("Mismatched tags: " + openMark.getElementName() + " != " + closeMark.getElementName());
                }

                // Créer l'élément final
                Element element;
                String content = contentBuilder.toString().trim();
                if (children.isEmpty() && !content.isEmpty()) {
                    // Élément avec contenu textuel uniquement
                    element = new Element(openMark.getElementName(), openMark.getAttributes(), content);
                } else if (!children.isEmpty() && !content.isEmpty()) {
                    // Élément avec contenu ET enfants
                    element = new Element(openMark.getElementName(), openMark.getAttributes(), content, children);
                } else {
                    // Élément avec des enfants uniquement (ou vide)
                    element = new Element(openMark.getElementName(), openMark.getAttributes(), children);
                }

                i++; // Avancer après le CLOSE_MARK
                return new ParseResult(element, i);

            } else {
                throw new IllegalStateException("Unexpected token type: " + currentType + " at position " + i);
            }
        }

        // Si on arrive ici, il manque un CLOSE_MARK
        throw new IllegalStateException("Missing CLOSE_MARK for element: " + openMark.getElementName());
    }
    
    private List<Element> extractAllElementsExceptRoot(Element rootElement) {
        List<Element> allElements = new ArrayList<>();
        collectAllElements(rootElement, allElements);
        
        // Le root est toujours en premier dans la traversée, donc on l'enlève
        if (!allElements.isEmpty()) {
            allElements.remove(0);
        }
        
        return allElements;
    }
    
    private void collectAllElements(Element element, List<Element> result) {
        result.add(element);
        for (Element child : element.getChildren()) {
            collectAllElements(child, result);
        }
    }

    private static class ParseResult {
        final Element element;
        final int nextIndex;

        ParseResult(Element element, int nextIndex) {
            this.element = element;
            this.nextIndex = nextIndex;
        }
    }
}
