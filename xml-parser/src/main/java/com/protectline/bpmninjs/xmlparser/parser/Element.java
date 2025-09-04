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
    
    public Element(String elementName, Map<String, String> attributes, String content) {
        this.elementName = elementName;
        this.attributes = attributes;
        this.content = content;
        this.children = new ArrayList<>();
        this.isSelfClosing = false;
        this.isXmlDeclaration = "xml".equals(elementName);
    }
    
    public Element(String elementName, Map<String, String> attributes, List<Element> children) {
        this.elementName = elementName;
        this.attributes = attributes;
        this.content = "";
        this.children = new ArrayList<>(children);
        this.isSelfClosing = false;
        this.isXmlDeclaration = "xml".equals(elementName);
    }
    
    public Element(String elementName, Map<String, String> attributes, boolean isSelfClosing) {
        this.elementName = elementName;
        this.attributes = attributes;
        this.content = "";
        this.children = new ArrayList<>();
        this.isSelfClosing = isSelfClosing;
        this.isXmlDeclaration = "xml".equals(elementName);
    }
    
    public Element(String elementName, Map<String, String> attributes, String content, List<Element> children) {
        this.elementName = elementName;
        this.attributes = attributes;
        this.content = content;
        this.children = new ArrayList<>(children);
        this.isSelfClosing = false;
        this.isXmlDeclaration = "xml".equals(elementName);
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
}
