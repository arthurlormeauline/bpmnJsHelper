package com.protectline.jsproject.parser;

import java.util.Map;

/**
 * Représente un élément parsé (une balise avec ses attributs et son contenu)
 */
public class Element {
    private final String elementName;
    private final Map<String, String> attributes;
    private final String content;
    
    public Element(String elementName, Map<String, String> attributes, String content) {
        this.elementName = elementName;
        this.attributes = attributes;
        this.content = content;
    }
    
    public String getElementName() {
        return elementName;
    }
    
    public Map<String, String> getAttributes() {
        return attributes;
    }
    
    public String getContent() {
        return content;
    }
    
    @Override
    public String toString() {
        return "Element{" +
                "elementName='" + elementName + '\'' +
                ", attributes=" + attributes +
                ", content='" + content + '\'' +
                '}';
    }
}