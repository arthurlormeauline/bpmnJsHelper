package com.protectline.bpmninjs.xmlparser;

import java.util.Map;

/**
 * Représente une balise auto-fermante (par exemple : <element attr="value"/>)
 */
public class SelfCloseMark {
    private final String elementName;
    private final Map<String, String> attributes;
    
    public SelfCloseMark(String elementName, Map<String, String> attributes) {
        this.elementName = elementName;
        this.attributes = attributes;
    }
    
    public String getElementName() {
        return elementName;
    }
    
    public Map<String, String> getAttributes() {
        return attributes;
    }
    
    @Override
    public String toString() {
        return "SelfCloseMark{" +
                "elementName='" + elementName + '\'' +
                ", attributes=" + attributes +
                '}';
    }
}