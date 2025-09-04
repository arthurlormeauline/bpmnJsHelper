package com.protectline.bpmninjs.xmlparser.lexer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.protectline.bpmninjs.xmlparser.lexer.TOKEN_TYPE.STRING;


/**
 * Lexer générique qui supporte plusieurs symboles pour un même type de token
 * Utilise un algorithme qui trouve TOUS les symboles dans le texte et les traite par position
 */
public class GenericLexer {

    public List<Token> tokenize(String content, List<TOKEN_TYPE> types, TokenDefinition def) {
        return tokenizeString(content, types, def);
    }

    private List<Token> tokenizeString(String content, List<TOKEN_TYPE> types, TokenDefinition def) {
        if (content.isEmpty()) {
            return new ArrayList<>();
        }

        // Construire la liste de tous les symboles possibles avec leurs types
        List<SymbolInfo> allSymbols = new ArrayList<>();
        for (TOKEN_TYPE type : types) {
            for (String symbol : def.getTypeValues(type)) {
                allSymbols.add(new SymbolInfo(symbol, type));
            }
        }

        // Trier par longueur décroissante pour prioriser les symboles plus longs
        allSymbols.sort(Comparator.comparingInt((SymbolInfo s) -> s.symbol.length()).reversed());

        // Trouver toutes les occurrences de tous les symboles
        List<SymbolMatch> matches = new ArrayList<>();
        for (SymbolInfo symbolInfo : allSymbols) {
            int index = 0;
            while (index < content.length()) {
                int foundIndex = content.indexOf(symbolInfo.symbol, index);
                if (foundIndex == -1) {
                    break;
                }
                matches.add(new SymbolMatch(foundIndex, foundIndex + symbolInfo.symbol.length(), symbolInfo));
                index = foundIndex + 1; // Continue searching from next position
            }
        }

        // Éliminer les matches qui se chevauchent, en gardant les plus longs
        List<SymbolMatch> finalMatches = new ArrayList<>();
        
        // Trier d'abord par position, puis par longueur décroissante
        matches.sort(Comparator.comparingInt((SymbolMatch m) -> m.start)
                .thenComparingInt((SymbolMatch m) -> m.symbolInfo.symbol.length()).reversed());
        
        for (SymbolMatch candidate : matches) {
            boolean shouldAdd = true;
            
            // Vérifier si ce candidat chevauche avec un match déjà accepté
            for (int i = 0; i < finalMatches.size(); i++) {
                SymbolMatch existing = finalMatches.get(i);
                
                if (candidate.overlaps(existing)) {
                    // Il y a chevauchement, garder le plus long
                    if (candidate.symbolInfo.symbol.length() > existing.symbolInfo.symbol.length()) {
                        // Remplacer l'existant par le candidat plus long
                        finalMatches.remove(i);
                        i--; // Ajuster l'index après suppression
                    } else {
                        // Garder l'existant, ignorer le candidat
                        shouldAdd = false;
                        break;
                    }
                }
            }
            
            if (shouldAdd) {
                finalMatches.add(candidate);
            }
        }

        // Retrier par position seulement
        finalMatches.sort(Comparator.comparingInt(m -> m.start));

        // Si aucun match trouvé, retourner le contenu comme STRING token
        if (finalMatches.isEmpty()) {
            return List.of(new Token(STRING, content));
        }

        // Construire les tokens résultants
        List<Token> result = new ArrayList<>();
        int currentPos = 0;

        for (SymbolMatch match : finalMatches) {
            // Ajouter le contenu avant le match comme STRING token (si non vide)
            if (match.start > currentPos) {
                String beforeMatch = content.substring(currentPos, match.start);
                if (!beforeMatch.isEmpty()) {
                    result.add(new Token(STRING, beforeMatch));
                }
            }

            // Ajouter le token du symbole trouvé
            result.add(new Token(match.symbolInfo.type, match.symbolInfo.symbol));
            currentPos = match.end;
        }

        // Ajouter le contenu restant comme STRING token (si non vide)
        if (currentPos < content.length()) {
            String remaining = content.substring(currentPos);
            if (!remaining.isEmpty()) {
                result.add(new Token(STRING, remaining));
            }
        }

        return result;
    }

    private static class SymbolInfo {
        final String symbol;
        final TOKEN_TYPE type;

        SymbolInfo(String symbol, TOKEN_TYPE type) {
            this.symbol = symbol;
            this.type = type;
        }
    }

    private static class SymbolMatch {
        final int start;
        final int end;
        final SymbolInfo symbolInfo;

        SymbolMatch(int start, int end, SymbolInfo symbolInfo) {
            this.start = start;
            this.end = end;
            this.symbolInfo = symbolInfo;
        }

        boolean overlaps(SymbolMatch other) {
            return this.start < other.end && this.end > other.start;
        }
    }
}
