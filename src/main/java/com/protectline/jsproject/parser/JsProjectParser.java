package com.protectline.jsproject.parser;

import com.protectline.common.block.Block;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser principal utilisant l'architecture Lexer -> TokenParser -> JsParser
 */
public class JsProjectParser {
    
    private final Lexer lexer;
    private final TokenParser tokenParser;
    private final JsParserFactory parserFactory;
    
    public JsProjectParser() throws IOException {
        this.lexer = new Lexer();
        this.tokenParser = new TokenParser();
        this.parserFactory = new JsParserFactory();
    }

    /**
     * Parse le contenu JS en blocs selon l'architecture lexer/parser
     */
    public List<Block> parseJsToBlocks(String jsContent) {
        // Étape 1: Lexer - Transformer le string en tokens
        List<Token> tokens = lexer.tokenize(jsContent);
        
        // Étape 2: TokenParser - Transformer les tokens en éléments
        List<Element> elements = tokenParser.parseTokensToElements(tokens);
        
        // Étape 3: Pour chaque élément, utiliser la factory pour créer le bon parser
        List<Block> allBlocks = new ArrayList<>();
        
        for (Element element : elements) {
            try {
                JsParser parser = parserFactory.createParser(element.getElementName());
                JsParser.ParseResult result = parser.parse(element.getContent(), element.getAttributes());
                allBlocks.addAll(result.getBlocks());
            } catch (Exception e) {
                // Si pas de parser pour cet élément, on l'ignore pour l'instant
                System.err.println("No parser found for element: " + element.getElementName());
            }
        }
        
        return allBlocks;
    }
}
