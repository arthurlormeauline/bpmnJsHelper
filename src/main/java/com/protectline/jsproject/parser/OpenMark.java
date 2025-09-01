package com.protectline.jsproject.parser;

import java.util.Map;

/**
 * Représente une balise ouvrante avec ses attributs
 */
class OpenMark {
    private final String elementName;
    private final Map<String, String> attributes;
    
    OpenMark(String elementName, Map<String, String> attributes) {
        this.elementName = elementName;
        this.attributes = attributes;
    }
    
    String getElementName() {
        return elementName;
    }
    
    Map<String, String> getAttributes() {
        return attributes;
    }
    
    @Override
    public String toString() {
        return "OpenMark{" +
                "elementName='" + elementName + '\'' +
                ", attributes=" + attributes +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpenMark openMark = (OpenMark) o;
        return elementName.equals(openMark.elementName) && attributes.equals(openMark.attributes);
    }
    
    @Override
    public int hashCode() {
        return elementName.hashCode() * 31 + attributes.hashCode();
    }
}