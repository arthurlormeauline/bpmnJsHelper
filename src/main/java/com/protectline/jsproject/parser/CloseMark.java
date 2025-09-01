package com.protectline.jsproject.parser;

/**
 * Représente une balise fermante
 */
class CloseMark {
    private final String elementName;
    
    CloseMark(String elementName) {
        this.elementName = elementName;
    }
    
    String getElementName() {
        return elementName;
    }
    
    @Override
    public String toString() {
        return "CloseMark{" +
                "elementName='" + elementName + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CloseMark closeMark = (CloseMark) o;
        return elementName.equals(closeMark.elementName);
    }
    
    @Override
    public int hashCode() {
        return elementName.hashCode();
    }
}