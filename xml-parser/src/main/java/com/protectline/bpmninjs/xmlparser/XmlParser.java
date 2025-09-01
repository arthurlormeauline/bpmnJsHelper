package com.protectline.bpmninjs.xmlparser;

import java.util.List;

public class XmlParser {

    private Lexer lexer;
    private TokenParser tokenParser;

    public List<Element> parseXml(String jsContent){
        List<Token> tokens = lexer.tokenize(jsContent);
        return tokenParser.parseTokensToElements(tokens);
    }
}
