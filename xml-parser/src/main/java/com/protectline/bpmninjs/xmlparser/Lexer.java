package com.protectline.bpmninjs.xmlparser;

import java.util.ArrayList;
import java.util.List;

/**
 * Lexer générique qui utilise une TokenDefinition pour parser différents formats
 */
public class Lexer {
    
    private final TokenDefinition tokenDefinition;
    private boolean insideTag = false;
    
    public Lexer(TokenDefinition tokenDefinition) {
        this.tokenDefinition = tokenDefinition;
    }
    
    public List<Token> tokenize(String content) {
        List<Token> tokens = new ArrayList<>();
        int i = 0;
        insideTag = false;
        
        while (i < content.length()) {
            // Essayer de matcher chaque type de token dans l'ordre de priorité
            TOKEN_TYPE[] tokenTypes = {TOKEN_TYPE.OPEN, TOKEN_TYPE.CLOSE, TOKEN_TYPE.END_SYMBOL, TOKEN_TYPE.EQUALS};
            
            boolean tokenFound = false;
            
            for (TOKEN_TYPE tokenType : tokenTypes) {
                if (tokenDefinition.matches(content, i, tokenType)) {
                    int consumedChars = tokenDefinition.getTokenLength(tokenType);
                    String tokenString = tokenDefinition.extractTokenString(content, i, consumedChars);
                    tokens.add(new Token(tokenType, tokenString));
                    i += consumedChars;
                    
                    // Gérer le contexte des balises
                    if (tokenType == TOKEN_TYPE.OPEN) {
                        insideTag = true;
                        parseElement(content, i, tokens);
                        // Avancer jusqu'après l'élément
                        i = skipToNextNonElementChar(content, i);
                    }
                    else if (tokenType == TOKEN_TYPE.CLOSE) {
                        insideTag = false;
                    }
                    // Si c'est un END_SYMBOL, parser l'élément qui suit aussi
                    else if (tokenType == TOKEN_TYPE.END_SYMBOL) {
                        parseElement(content, i, tokens);
                        // Avancer jusqu'après l'élément
                        i = skipToNextNonElementChar(content, i);
                    }
                    
                    tokenFound = true;
                    break;
                }
            }
            
            if (!tokenFound) {
                // Collecter le contenu string jusqu'au prochain token reconnu
                i = parseStringContent(content, i, tokens);
            }
        }
        
        return tokens;
    }
    
    private void parseElement(String content, int startPos, List<Token> tokens) {
        StringBuilder elementBuilder = new StringBuilder();
        int i = startPos;
        
        while (i < content.length()) {
            char c = content.charAt(i);
            if (Character.isWhitespace(c) || c == '>' || c == '/') {
                break;
            }
            elementBuilder.append(c);
            i++;
        }
        
        String elementName = elementBuilder.toString();
        if (!elementName.isEmpty()) {
            tokens.add(new Token(TOKEN_TYPE.ELEMENT, elementName));
        }
    }
    
    private int skipToNextNonElementChar(String content, int startPos) {
        int i = startPos;
        while (i < content.length()) {
            char c = content.charAt(i);
            if (Character.isWhitespace(c) || c == '>' || c == '/') {
                break;
            }
            i++;
        }
        return i;
    }
    
    private int parseStringContent(String content, int startPos, List<Token> tokens) {
        StringBuilder stringBuilder = new StringBuilder();
        int i = startPos;
        boolean inQuotes = false;
        char quoteChar = '"';
        
        while (i < content.length()) {
            char currentChar = content.charAt(i);
            
            // Gérer les guillemets
            if (currentChar == '"' || currentChar == '\'') {
                if (!inQuotes) {
                    // Ajouter le token précédent s'il y en a un
                    if (stringBuilder.length() > 0) {
                        tokens.add(new Token(TOKEN_TYPE.STRING, stringBuilder.toString()));
                        stringBuilder = new StringBuilder();
                    }
                    inQuotes = true;
                    quoteChar = currentChar;
                    stringBuilder.append(currentChar);
                    i++;
                    continue;
                } else if (currentChar == quoteChar) {
                    stringBuilder.append(currentChar);
                    inQuotes = false;
                    // Ajouter le token quoté
                    tokens.add(new Token(TOKEN_TYPE.STRING, stringBuilder.toString()));
                    stringBuilder = new StringBuilder();
                    i++;
                    continue;
                }
            }
            
            if (inQuotes) {
                stringBuilder.append(currentChar);
                i++;
                continue;
            }
            
            // Vérifier si on rencontre un token reconnu
            boolean foundToken = false;
            
            for (TOKEN_TYPE tokenType : TOKEN_TYPE.values()) {
                if (tokenType == TOKEN_TYPE.ELEMENT || tokenType == TOKEN_TYPE.STRING || 
                    tokenType == TOKEN_TYPE.OPEN_MARK || tokenType == TOKEN_TYPE.CONTENT || 
                    tokenType == TOKEN_TYPE.CLOSE_MARK) {
                    continue; // Skip les tokens complexes
                }
                
                if (tokenDefinition.matches(content, i, tokenType)) {
                    foundToken = true;
                    break;
                }
            }
            
            if (foundToken) {
                break;
            }
            
            // Si c'est un espace et qu'on a déjà du contenu, terminer ce token SEULEMENT si on est dans une balise
            if (insideTag && Character.isWhitespace(currentChar) && stringBuilder.length() > 0) {
                tokens.add(new Token(TOKEN_TYPE.STRING, stringBuilder.toString()));
                stringBuilder = new StringBuilder();
                // Collecter les espaces
                while (i < content.length() && Character.isWhitespace(content.charAt(i))) {
                    stringBuilder.append(content.charAt(i));
                    i++;
                }
                if (stringBuilder.length() > 0) {
                    tokens.add(new Token(TOKEN_TYPE.STRING, stringBuilder.toString()));
                    stringBuilder = new StringBuilder();
                }
                continue;
            }
            
            stringBuilder.append(currentChar);
            i++;
        }
        
        String stringValue = stringBuilder.toString();
        if (!stringValue.isEmpty()) {
            tokens.add(new Token(TOKEN_TYPE.STRING, stringValue));
        }
        
        return i;
    }
}
