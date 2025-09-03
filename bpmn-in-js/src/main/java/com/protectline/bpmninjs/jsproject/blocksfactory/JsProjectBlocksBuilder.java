package com.protectline.bpmninjs.jsproject.blocksfactory;

import com.protectline.bpmninjs.application.mainfactory.MainFactory;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.xmlparser.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsProjectBlocksBuilder {
    
    private final MainFactory mainFactory;
    private XmlParser xmlParser;

    public JsProjectBlocksBuilder(MainFactory mainFactory) throws IOException {
        this.mainFactory = mainFactory;
        this.xmlParser = new XmlParser();
    }

    public List<Block> parseJsToBlocks(String jsContent) {

        List<Block> allBlocks = new ArrayList<>();
        List<Element> elements = xmlParser.parseXml(jsContent, new JsProjectTokenDefinition());
        
        for (Element element : elements) {
            try {
                BlockFromElement parser = mainFactory.getBlockBuilder(element.getElementName());
                BlockFromElementResult result = parser.parse(element.getContent(), element.getAttributes());
                allBlocks.addAll(result.getBlocks());
            } catch (Exception e) {
                throw new IllegalArgumentException("No parser found for element: " + element.getElementName());
            }
        }
        
        return allBlocks;
    }
}
