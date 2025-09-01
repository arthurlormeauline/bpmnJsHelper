package com.protectline.bpmninjs.xmlparser;

import java.util.List;

public class XmlParser {

    private TokenParser tokenParser;

    public XmlParser() {
        this.tokenParser = new TokenParser();
    }

    public List<Element> parseXml(String jsContent, TokenDefinition tokenDefinition){
        List<Token> tokens = new Lexer(tokenDefinition).tokenize(jsContent);
        return tokenParser.parseTokensToElements(tokens);
    }
}
