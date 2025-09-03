package com.protectline.bpmninjs.jsproject;

import java.util.Map;

public interface JsNode {
    
    String getElementName();
    
    Map<String, String> getAttributes();
    
    String getContent();
}