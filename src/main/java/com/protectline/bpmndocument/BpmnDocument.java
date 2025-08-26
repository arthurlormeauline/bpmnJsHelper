package com.protectline.bpmndocument;

import com.protectline.bpmndocument.model.Node;

import java.util.List;

public interface BpmnDocument {
    List<Node> getFirstLevelElements();
}
