package com.protectline.bpmninjs.jsproject.blocksfactory;

import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.files.FileUtil;
import com.protectline.bpmninjs.xmlparser.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser principal utilisant l'architecture Lexer -> TokenParser -> JsParser
 */
public class JsProjectBlocksBuilder {
    
    private final Lexer lexer;
    private final TokenParser tokenParser;
    private final BlockFromElementFactory parserFactory;
    private XmlParser xmlParser;

    public JsProjectBlocksBuilder(FileUtil fileUtil) throws IOException {
        this.lexer = new Lexer();
        this.tokenParser = new TokenParser();
        this.parserFactory = new BlockFromElementFactory(fileUtil);
        this.xmlParser = new XmlParser();
    }


    public List<Block> parseJsToBlocks(String jsContent) {

        List<Block> allBlocks = new ArrayList<>();
        List<Element> elements = xmlParser.parseXml(jsContent);
        
        for (Element element : elements) {
            try {
                BlockFromElementBuilder parser = parserFactory.getBlockBuilder(element.getElementName());
                BlockFromElementResult result = parser.parse(element.getContent(), element.getAttributes());
                allBlocks.addAll(result.getBlocks());
            } catch (Exception e) {
                System.err.println("No parser found for element: " + element.getElementName());
            }
        }
        
        return allBlocks;
    }
}
