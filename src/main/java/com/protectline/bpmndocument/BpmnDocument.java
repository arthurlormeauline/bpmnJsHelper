package com.protectline.bpmndocument;

import com.protectline.bpmndocument.model.Node;
import com.protectline.jsproject.model.block.Block;

import java.util.List;

public interface BpmnDocument {
    List<Node> getFirstLevelElements();

    Node getElement(String id);
}
