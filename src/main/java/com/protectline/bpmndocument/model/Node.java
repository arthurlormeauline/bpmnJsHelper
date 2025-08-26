package com.protectline.bpmndocument.model;

import java.util.List;

public interface Node {
    String getNodeName();

    String getAttributeValue(String id);

    List<String> getScripts();
}
