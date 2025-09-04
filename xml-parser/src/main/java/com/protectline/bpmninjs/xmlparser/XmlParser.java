package com.protectline.bpmninjs.xmlparser;

import java.util.List;

import static com.protectline.bpmninjs.xmlparser.TOKEN_TYPE.*;

public class XmlParser {

    private TokenParser tokenParser;

    public XmlParser() {
        this.tokenParser = new TokenParser();
    }

    public List<Element> parseXml(String jsContent, TokenDefinition tokenDefinition){
        GenericLexer lexer = new GenericLexer();
        List<Token> tokens = lexer.tokenize(jsContent, List.of(CLOSE,OPEN,EQUALS,END_SYMBOL), tokenDefinition);
        return tokenParser.parseXmlAndGetElements(tokens);
    }
    
    public Element getRootElement(String xmlContent, TokenDefinition tokenDefinition) {
        GenericLexer lexer = new GenericLexer();
        List<Token> tokens = lexer.tokenize(xmlContent, List.of(CLOSE,OPEN,EQUALS,END_SYMBOL), tokenDefinition);
        return tokenParser.parseXml(tokens);
    }
}
