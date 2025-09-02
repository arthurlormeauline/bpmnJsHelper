package com.protectline.bpmninjs.xmlparser;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.protectline.bpmninjs.xmlparser.TOKEN_TYPE.*;

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
        var genLexer = new GenericLexer();
        return genLexer.tokenize(content, List.of(OPEN,CLOSE,END_SYMBOL,EQUALS), tokenDefinition);
    }

    private List<Token> tokenizeToWordsAndBlanks(String content) {
        List<Token> tokens = new ArrayList<>();
        int i = 0;
        
        while (i < content.length()) {
            char c = content.charAt(i);

            if (Character.isWhitespace(c)) {
                // Collecter tous les espaces consécutifs
                StringBuilder blankBuilder = new StringBuilder();
                while (i < content.length() && Character.isWhitespace(content.charAt(i))) {
                    blankBuilder.append(content.charAt(i));
                    i++;
                }
                tokens.add(new Token(TOKEN_TYPE.BLANK, blankBuilder.toString()));
            } else {
                // Collecter tous les caractères non-blancs consécutifs
                StringBuilder wordBuilder = new StringBuilder();
                while (i < content.length() && !Character.isWhitespace(content.charAt(i))) {
                    wordBuilder.append(content.charAt(i));
                    i++;
                }
                tokens.add(new Token(TOKEN_TYPE.WORD, wordBuilder.toString()));
            }
        }
        
        return tokens;
    }

    @AllArgsConstructor
    @Getter
    class WordParsingResult{
        Token wordsAtBeginning;
        List<Token> specializedToken;
        Token wordsAtTheEnd;
    }

    private List<Token> analyzeSemanticTokens(List<Token> basicTokens) {
        List<Token> semanticTokens = new ArrayList<>();
        
        for (Token token : basicTokens) {
            if (token.getType() == TOKEN_TYPE.WORD) {
                // Parser le WORD pour identifier les tokens spéciaux
                List<Token> parsedTokens = parseWordToTokens(token);
                semanticTokens.addAll(parsedTokens);
            } else if (token.getType() == TOKEN_TYPE.BLANK) {
                // Convertir BLANK en STRING
                semanticTokens.add(new Token(TOKEN_TYPE.STRING, token.getValue()));
            } else {
                // Autres types de tokens, les passer tels quels
                semanticTokens.add(token);
            }
        }
        
        return semanticTokens;
    }
    
    private List<Token> parseWordToTokens(Token wordToken) {
        List<Token> result = new ArrayList<>();
        String word = wordToken.getValue();
        int i = 0;
        
        StringBuilder currentString = new StringBuilder();
        
        while (i < word.length()) {
            boolean foundSpecialToken = false;
            TOKEN_TYPE[] tokenTypes = {OPEN, CLOSE, END_SYMBOL, TOKEN_TYPE.EQUALS};
            
            for (TOKEN_TYPE tokenType : tokenTypes) {
                if (tokenDefinition.matches(word, i, tokenType)) {
                    // Avant d'ajouter le token spécial, ajouter la string accumulée
                    if (currentString.length() > 0) {
                        result.add(new Token(TOKEN_TYPE.STRING, currentString.toString()));
                        currentString = new StringBuilder();
                    }
                    
                    // Ajouter le token spécial
                    int tokenLength = tokenDefinition.getTokenLength(tokenType);
                    String tokenString = tokenDefinition.extractTokenString(word, i, tokenLength);
                    result.add(new Token(tokenType, tokenString));
                    i += tokenLength;
                    foundSpecialToken = true;
                    break;
                }
            }
            
            if (!foundSpecialToken) {
                // Caractère normal, l'ajouter à la string courante
                currentString.append(word.charAt(i));
                i++;
            }
        }
        
        // Ajouter la dernière string si elle n'est pas vide
        if (currentString.length() > 0) {
            result.add(new Token(TOKEN_TYPE.STRING, currentString.toString()));
        }
        
        return result;
    }
    
    private List<Token> mergeAdjacentStringTokens(List<Token> tokens) {
        List<Token> merged = new ArrayList<>();
        StringBuilder stringBuffer = new StringBuilder();
        
        for (Token token : tokens) {
            if (token.getType() == TOKEN_TYPE.STRING) {
                stringBuffer.append(token.getValue());
            } else {
                // Token non-STRING : vider le buffer d'abord
                if (stringBuffer.length() > 0) {
                    merged.add(new Token(TOKEN_TYPE.STRING, stringBuffer.toString()));
                    stringBuffer = new StringBuilder();
                }
                merged.add(token);
            }
        }
        
        // Vider le buffer final
        if (stringBuffer.length() > 0) {
            merged.add(new Token(TOKEN_TYPE.STRING, stringBuffer.toString()));
        }
        
        return merged;
    }

    private WordParsingResult parseWord(Token token) {
        var word = token.getValue();
        return parseWordRecursive(word, 0);
    }
    
    private WordParsingResult parseWordRecursive(String word, int startIndex) {
        List<Token> allSpecializedTokens = new ArrayList<>();
        int i = startIndex;
        
        // 1. Collecter les caractères normaux au début (avant le premier token spécial)
        StringBuilder wordsAtBeginning = new StringBuilder();
        boolean foundFirstSpecialToken = false;
        
        while (i < word.length() && !foundFirstSpecialToken) {
            TOKEN_TYPE[] tokenTypes = {OPEN, CLOSE, END_SYMBOL, TOKEN_TYPE.EQUALS};
            boolean isSpecialToken = false;
            
            for (TOKEN_TYPE tokenType : tokenTypes) {
                if (tokenDefinition.matches(word, i, tokenType)) {
                    foundFirstSpecialToken = true;
                    isSpecialToken = true;
                    break;
                }
            }
            
            if (!isSpecialToken) {
                wordsAtBeginning.append(word.charAt(i));
                i++;
            }
        }
        
        // 2. Parser les tokens spéciaux consécutifs
        while (i < word.length()) {
            boolean foundSpecialToken = false;
            TOKEN_TYPE[] tokenTypes = {OPEN, CLOSE, END_SYMBOL, TOKEN_TYPE.EQUALS};
            
            for (TOKEN_TYPE tokenType : tokenTypes) {
                if (tokenDefinition.matches(word, i, tokenType)) {
                    int tokenLength = tokenDefinition.getTokenLength(tokenType);
                    String tokenString = tokenDefinition.extractTokenString(word, i, tokenLength);
                    allSpecializedTokens.add(new Token(tokenType, tokenString));
                    i += tokenLength;
                    foundSpecialToken = true;
                    break;
                }
            }
            
            if (!foundSpecialToken) {
                // On a atteint des caractères normaux après des tokens spéciaux
                // Il faut parser récursivement le reste pour détecter d'autres tokens spéciaux éventuels
                if (i < word.length()) {
                    WordParsingResult remainingResult = parseWordRecursive(word, i);
                    
                    // Fusionner les résultats
                    if (remainingResult.getWordsAtBeginning() != null) {
                        // Il y a des mots normaux au début du parsing récursif
                        // Ces mots doivent être traités comme un token STRING intermédiaire
                        String intermediateWord = remainingResult.getWordsAtBeginning().getValue();
                        allSpecializedTokens.add(new Token(TOKEN_TYPE.STRING, intermediateWord));
                        
                        // Ajouter les tokens spéciaux du parsing récursif
                        allSpecializedTokens.addAll(remainingResult.getSpecializedToken());
                        
                        Token beginningToken = wordsAtBeginning.length() > 0 ? new Token(TOKEN_TYPE.WORD, wordsAtBeginning.toString()) : null;
                        return new WordParsingResult(beginningToken, allSpecializedTokens, remainingResult.getWordsAtTheEnd());
                    } else {
                        // Pas de mots au début dans le parsing récursif, donc il n'y avait que des tokens spéciaux
                        allSpecializedTokens.addAll(remainingResult.getSpecializedToken());
                        Token beginningToken = wordsAtBeginning.length() > 0 ? new Token(TOKEN_TYPE.WORD, wordsAtBeginning.toString()) : null;
                        return new WordParsingResult(beginningToken, allSpecializedTokens, remainingResult.getWordsAtTheEnd());
                    }
                }
                break;
            }
        }
        
        // 3. Si on arrive ici, il n'y avait pas de caractères normaux après les tokens spéciaux
        Token beginningToken = wordsAtBeginning.length() > 0 ? new Token(TOKEN_TYPE.WORD, wordsAtBeginning.toString()) : null;
        return new WordParsingResult(beginningToken, allSpecializedTokens, null);
    }

    private int parseElementAfterOpen(List<Token> basicTokens, int startIndex, List<Token> semanticTokens) {
        int i = startIndex;

        // Ignorer les espaces
        while (i < basicTokens.size() && basicTokens.get(i).getType() == TOKEN_TYPE.BLANK) {
            i++;
        }

        // Le prochain WORD devrait être l'élément
        if (i < basicTokens.size() && basicTokens.get(i).getType() == TOKEN_TYPE.WORD) {
            semanticTokens.add(new Token(TOKEN_TYPE.ELEMENT, basicTokens.get(i).getValue()));
            i++;
        }

        return i;
    }


    // Cette méthode n'est plus utilisée avec la nouvelle approche simplifiée
    private int accumulateStringTokens(List<Token> basicTokens, int startIndex, List<Token> semanticTokens) {
        // Legacy method - not used anymore
        return startIndex + 1;
    }
}
