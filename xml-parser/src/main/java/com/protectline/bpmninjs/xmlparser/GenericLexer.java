package com.protectline.bpmninjs.xmlparser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.protectline.bpmninjs.xmlparser.TOKEN_TYPE.STRING;

public class GenericLexer {

    public List<Token> tokenize(String content, List<TOKEN_TYPE> types, TokenDefinition def) {
        Token stringTokenToTokenize = new Token(STRING, content);
        return recTokenize(stringTokenToTokenize, 0, types, def);
    }

    private List<Token> recTokenize(Token token, int indexType, List<TOKEN_TYPE> types, TokenDefinition def) {
        List interResult = new ArrayList<Token>();
        manageStringToken(indexType, types, def, token, interResult);
        return interResult;
    }

    private void manageStringToken(int indexType, List<TOKEN_TYPE> types, TokenDefinition def, Token token, List interResult) {
        if (indexType < types.size()) {
            TOKEN_TYPE typeUseToSplit = types.get(indexType);
            var splitTokenValue = def.getTypeValue(typeUseToSplit);
            String[] splittedArray = token.getStringValue().split(Pattern.quote(splitTokenValue), -1);
            List<String> splittedString = new ArrayList<>();

            for (int i = 0; i < splittedArray.length; i++) {
                if (i > 0) {
                    splittedString.add(splitTokenValue); // Add delimiter between tokens
                }
                if (!splittedArray[i].isEmpty()) {
                    splittedString.add(splittedArray[i]);
                }
            }
            var firstToken = splittedString.get(0);
            var tokenTypeOfFirstToken = def.getType((firstToken));
            var beginWithSplitter = tokenTypeOfFirstToken.equals(typeUseToSplit);
            int count = 0;

            if (splittedString.size() == 1) {
                // Pas de split sur ce type, continuer avec le type suivant
                parseString(indexType + 1, types, def, interResult, token.getStringValue());
            } else {
                if (!beginWithSplitter) {
                    count = 1;
                }

                for (int splittedStringIndex = 0; splittedStringIndex < splittedString.size(); splittedStringIndex++) {
                    var stringTokenToParse = splittedString.get(splittedStringIndex);

                    if (stringTokenToParse.equals(splitTokenValue)) { // Check if it's actually the delimiter
                        interResult.add(new Token(typeUseToSplit, stringTokenToParse));
                    } else { // should be string to parse recursively
                        parseString(indexType + 1, types, def, interResult, stringTokenToParse);
                    }
                }
            }

        } else {
            interResult.add(token);
        }
    }

    private void parseString(int indexType, List<TOKEN_TYPE> types, TokenDefinition def, List interResult, String strinTokenToParse) {
        var stringToken = new Token(STRING, strinTokenToParse);
        interResult.addAll(recTokenize(stringToken, indexType, types, def));
    }

}
