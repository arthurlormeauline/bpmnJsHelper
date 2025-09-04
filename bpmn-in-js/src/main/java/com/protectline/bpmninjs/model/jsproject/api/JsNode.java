package com.protectline.bpmninjs.model.jsproject.api;

import java.util.Map;

public interface JsNode {
    
    String getElementName();
    
    Map<String, String> getAttributes();
    
    String getContent();
}
