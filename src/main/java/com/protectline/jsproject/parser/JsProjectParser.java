package com.protectline.jsproject.parser;

import com.protectline.common.block.Block;
import com.protectline.files.FileUtil;

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
    
    public JsProjectParser(FileUtil fileUtil) throws IOException {
        this.lexer = new Lexer();
        this.tokenParser = new TokenParser();
        this.parserFactory = new JsParserFactory(fileUtil);
    }

    /**
     * Parse le contenu JS en blocs selon l'architecture lexer/parser
     */
    public List<Block> parseJsToBlocks(String jsContent) {
        List<Token> tokens = lexer.tokenize(jsContent);
        
        List<Element> elements = tokenParser.parseTokensToElements(tokens);
        
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
