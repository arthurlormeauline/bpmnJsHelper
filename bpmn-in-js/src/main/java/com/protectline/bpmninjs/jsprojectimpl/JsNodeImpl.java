package com.protectline.bpmninjs.jsprojectimpl;

import com.protectline.bpmninjs.jsproject.JsNode;
import com.protectline.bpmninjs.xmlparser.Element;

import java.util.Map;

public class JsNodeImpl implements JsNode {
    
    private final Element element;
    
    public JsNodeImpl(Element element) {
        this.element = element;
    }
    
    @Override
    public String getElementName() {
        return element.getElementName();
    }
    
    @Override
    public Map<String, String> getAttributes() {
        return element.getAttributes();
    }
    
    @Override
    public String getContent() {
        return element.getContent();
    }
    
    @Override
    public String toString() {
        return element.toString();
    }
}
