package com.protectline.bpmninjs.xmlparser;

import java.util.ArrayList;
import java.util.List;


public class TokenParser {

    private final TokenSimplifier tokenSimplifier;

    public TokenParser() {
        this.tokenSimplifier = new TokenSimplifier();
    }

    public List<Element> parseTokensToElements(List<Token> tokens) {
        List<Token> simplifiedTokens = tokenSimplifier.simplifyTokens(tokens);
        List<Element> elements = new ArrayList<>();
        int indexOfFirstOpenMark = getIndexOfFirstOpenMark(simplifiedTokens);
        parseElementsRecursively(simplifiedTokens, indexOfFirstOpenMark, elements);

        return elements;
    }

    private static int getIndexOfFirstOpenMark(List<Token> simplifiedTokens) {
        int i = 0;
        boolean shouldContinue = true;
        while (shouldContinue) {
            if (simplifiedTokens.get(i).getType() != TOKEN_TYPE.OPEN_MARK) {
                i++;
            } else {
                shouldContinue = false;
            }
        }
        return i;
    }

    private int parseElementsRecursively(List<Token> tokens, int startIndex, List<Element> elements) {
        int i = startIndex;

        while (i < tokens.size()) {
            if (tokens.get(i).getType() != TOKEN_TYPE.OPEN_MARK) {
                throw new IllegalStateException("Expected OPEN_MARK at position " + i + ", but found: " + tokens.get(i).getType());
            }

            OpenMark openMark = tokens.get(i).getOpenMark();
            i++; // Avancer après l'OPEN_MARK

            StringBuilder contentBuilder = new StringBuilder();

            // 2. Après OPEN_MARK, traiter les tokens jusqu'au CLOSE_MARK correspondant
            while (i < tokens.size()) {
                TOKEN_TYPE currentType = tokens.get(i).getType();

                if (currentType == TOKEN_TYPE.CONTENT) {
                    // Ajouter le contenu au StringBuilder
                    contentBuilder.append(tokens.get(i).getStringValue());
                    i++;

                } else if (currentType == TOKEN_TYPE.OPEN_MARK) {
                    // Récursion sur l'élément enfant
                    i = parseElementsRecursively(tokens, i, elements);

                } else if (currentType == TOKEN_TYPE.CLOSE_MARK) {
                    // Vérifier que l'élément correspond et créer l'Element
                    CloseMark closeMark = tokens.get(i).getCloseMark();

                    if (!openMark.getElementName().equals(closeMark.getElementName())) {
                        throw new IllegalStateException("Mismatched tags: " + openMark.getElementName() + " != " + closeMark.getElementName());
                    }

                    // Créer l'élément avec le contenu final
                    String content = contentBuilder.toString();
                    Element element = new Element(openMark.getElementName(), openMark.getAttributes(), content);
                    elements.add(element);

                    i++; // Avancer après le CLOSE_MARK
                    return i; // Sortir de la récursion

                } else {
                    throw new IllegalStateException("Unexpected token type: " + currentType + " at position " + i);
                }
            }

            // Si on arrive ici, il manque un CLOSE_MARK
            throw new IllegalStateException("Missing CLOSE_MARK for element: " + openMark.getElementName());
        }

        return i;
    }
}
