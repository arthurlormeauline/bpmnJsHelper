package com.protectline.bpmninjs.xmlparser;

import java.util.List;

public class XmlParser {

    private TokenParser tokenParser;

    public XmlParser() {
        this.tokenParser = new TokenParser();
    }

    public List<Element> parseXml(String jsContent, TokenDefinition tokenDefinition){
        List<Token> tokens = new Lexer(tokenDefinition).tokenize(jsContent);
        System.out.println("DEBUG XmlParser: Lexer found " + tokens.size() + " tokens");
        List<Element> elements = tokenParser.parseTokensToElements(tokens);
        System.out.println("DEBUG XmlParser: TokenParser created " + elements.size() + " elements");
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            System.out.println("DEBUG XmlParser: Element " + i + ": '" + element.getElementName() + 
                             "' with attributes: " + element.getAttributes() +
                             " and content length: " + (element.getContent() != null ? element.getContent().length() : "null"));
        }
        
        return elements;
    }
}
