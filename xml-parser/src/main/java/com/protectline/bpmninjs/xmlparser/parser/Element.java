package com.protectline.bpmninjs.xmlparser.parser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Représente un élément parsé (une balise avec ses attributs, son contenu et ses enfants)
 * Structure d'arbre récursive compatible avec le format XML
 */
public class Element {
    private final String elementName;
    private final Map<String, String> attributes;
    private final String content;
    private final List<Element> children;
    private final boolean isSelfClosing;
    private final boolean isXmlDeclaration;
    
    
    public String getElementName() {
        return elementName;
    }
    
    public Map<String, String> getAttributes() {
        return attributes;
    }
    
    public String getContent() {
        return content;
    }
    
    public List<Element> getChildren() {
        return children;
    }
    
    public boolean isSelfClosing() {
        return isSelfClosing;
    }
    
    public boolean isXmlDeclaration() {
        return isXmlDeclaration;
    }
    
    public void addChild(Element child) {
        children.add(child);
    }
    
    /**
     * Retourne une liste plate de tous les éléments (y compris les enfants)
     * pour maintenir la compatibilité avec l'ancienne API
     */
    public List<Element> getElements() {
        List<Element> allElements = new ArrayList<>();
        collectAllElements(this, allElements);
        return allElements;
    }
    
    private void collectAllElements(Element element, List<Element> result) {
        result.add(element);
        for (Element child : element.getChildren()) {
            collectAllElements(child, result);
        }
    }
    
    /**
     * Génère la représentation XML de cet élément et de ses enfants
     */
    public String toXml() {
        return toXml(0);
    }
    
    /**
     * Génère la représentation XML avec indentation
     */
    private String toXml(int indentLevel) {
        StringBuilder xml = new StringBuilder();
        String indent = "  ".repeat(indentLevel);
        
        // Cas spécial pour l'élément root fictif (nom vide)
        if (elementName.isEmpty()) {
            for (Element child : children) {
                xml.append(child.toXml(indentLevel));
            }
            return xml.toString();
        }
        
        // Cas spécial pour les déclarations XML
        if (isXmlDeclaration) {
            xml.append("<?").append(elementName);
            appendAttributes(xml);
            xml.append("?>\n");
        } else if (isSelfClosing) {
            // Balise auto-fermante : <element attr="value" />
            xml.append(indent).append("<").append(elementName);
            appendAttributes(xml);
            xml.append(" />\n");
        } else if (children.isEmpty() && content.isEmpty()) {
            // Balise vide : <element></element>
            xml.append(indent).append("<").append(elementName);
            appendAttributes(xml);
            xml.append("></").append(elementName).append(">\n");
        } else if (children.isEmpty() && !content.isEmpty()) {
            // Balise avec contenu uniquement : <element>content</element>
            xml.append(indent).append("<").append(elementName);
            appendAttributes(xml);
            xml.append(">").append(content).append("</").append(elementName).append(">\n");
        } else {
            // Balise avec enfants
            xml.append(indent).append("<").append(elementName);
            appendAttributes(xml);
            xml.append(">\n");
            
            // Ajouter les enfants avec indentation
            for (Element child : children) {
                xml.append(child.toXml(indentLevel + 1));
            }
            
            xml.append(indent).append("</").append(elementName).append(">\n");
        }
        
        return xml.toString();
    }
    
    private void appendAttributes(StringBuilder xml) {
        for (Map.Entry<String, String> attr : attributes.entrySet()) {
            xml.append(" ").append(attr.getKey()).append("=\"").append(attr.getValue()).append("\"");
        }
    }
    
    @Override
    public String toString() {
        return "Element{" +
                "elementName='" + elementName + '\'' +
                ", attributes=" + attributes +
                ", content='" + content + '\'' +
                ", children=" + children.size() +
                ", isSelfClosing=" + isSelfClosing +
                '}';
    }
    
    /**
     * Builder pattern pour construire des Elements de façon flexible
     */
    public static class Builder {
        private String elementName = "";
        private Map<String, String> attributes = new LinkedHashMap<>();
        private String content = "";
        private List<Element> children = new ArrayList<>();
        private boolean isSelfClosing = false;
        private boolean isXmlDeclaration = false;
        
        private Builder() {}
        
        private Builder(Element source) {
            this.elementName = source.elementName;
            this.attributes = new LinkedHashMap<>(source.attributes);
            this.content = source.content;
            this.children = new ArrayList<>(source.children);
            this.isSelfClosing = source.isSelfClosing;
            this.isXmlDeclaration = source.isXmlDeclaration;
        }
        
        /**
         * Crée un nouveau builder vide
         */
        public static Builder builder() {
            return new Builder();
        }
        
        /**
         * Crée un builder à partir d'un Element existant (pattern copie)
         */
        public static Builder builder(Element source) {
            return new Builder(source);
        }
        
        public Builder withElementName(String elementName) {
            this.elementName = elementName;
            // Auto-detect XML declaration
            if ("xml".equals(elementName)) {
                this.isXmlDeclaration = true;
            }
            return this;
        }
        
        public Builder withAttributes(Map<String, String> attributes) {
            this.attributes = new LinkedHashMap<>(attributes);
            return this;
        }
        
        public Builder withAttribute(String key, String value) {
            this.attributes.put(key, value);
            return this;
        }
        
        public Builder withContent(String content) {
            this.content = content;
            return this;
        }
        
        public Builder withChildren(List<Element> children) {
            this.children = new ArrayList<>(children);
            return this;
        }
        
        public Builder withChild(Element child) {
            this.children.add(child);
            return this;
        }
        
        public Builder withSelfClosing(boolean isSelfClosing) {
            this.isSelfClosing = isSelfClosing;
            return this;
        }
        
        public Builder withXmlDeclaration(boolean isXmlDeclaration) {
            this.isXmlDeclaration = isXmlDeclaration;
            return this;
        }
        
        public Element build() {
            return new Element(elementName, attributes, content, children, isSelfClosing, isXmlDeclaration);
        }
    }
    
    /**
     * Constructeur privé utilisé par le Builder
     */
    private Element(String elementName, Map<String, String> attributes, String content, 
                   List<Element> children, boolean isSelfClosing, boolean isXmlDeclaration) {
        this.elementName = elementName;
        this.attributes = new LinkedHashMap<>(attributes);
        this.content = content;
        this.children = new ArrayList<>(children);
        this.isSelfClosing = isSelfClosing;
        this.isXmlDeclaration = isXmlDeclaration;
    }
}
