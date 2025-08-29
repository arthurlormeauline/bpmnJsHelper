package com.protectline.jsproject.parser;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    
    public List<Token> tokenize(String content) {
        List<Token> tokens = new ArrayList<>();
        int i = 0;
        
        while (i < content.length()) {
            char current = content.charAt(i);
            
            if (current == '/') {
                // Check for //
                if (i + 1 < content.length() && content.charAt(i + 1) == '/') {
                    tokens.add(new Token(TOKEN_TYPE.SLASH_SLASH, "//"));
                    i += 2;
                } else {
                    tokens.add(new Token(TOKEN_TYPE.SLASH, "/"));
                    i++;
                }
            } else if (current == '<') {
                tokens.add(new Token(TOKEN_TYPE.OPEN, "<"));
                i++;
                
                // Après un <, le prochain mot sans espace est un ELEMENT
                StringBuilder elementBuilder = new StringBuilder();
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
                
            } else if (current == '>') {
                tokens.add(new Token(TOKEN_TYPE.CLOSE, ">"));
                i++;
            } else if (current == '=') {
                tokens.add(new Token(TOKEN_TYPE.EQUALS, "="));
                i++;
            } else {
                // Collect string content until we hit a special character
                StringBuilder stringBuilder = new StringBuilder();
                while (i < content.length()) {
                    char c = content.charAt(i);
                    if (c == '/' || c == '<' || c == '>' || c == '=') {
                        // Pour les /, on vérifie si c'est un // avant de casser
                        if (c == '/' && i + 1 < content.length() && content.charAt(i + 1) == '/') {
                            break;
                        } else if (c != '/') {
                            break;
                        }
                    }
                    stringBuilder.append(c);
                    i++;
                }
                
                String stringValue = stringBuilder.toString();
                if (!stringValue.isEmpty()) {
                    tokens.add(new Token(TOKEN_TYPE.STRING, stringValue));
                }
            }
        }
        
        return tokens;
    }
}