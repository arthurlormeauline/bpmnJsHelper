package com.protectline.bpmninjs.model.bpmndocument.api.model;

import java.util.List;

public interface Node {
    String getNodeName();

    String getAttributeValue(String id);

    List<String> getScripts();

    NodeType getType();
}
