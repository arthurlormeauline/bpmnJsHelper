package com.protectline.bpmndocument;

import com.protectline.bpmndocument.model.Node;
import com.protectline.jsproject.model.block.Block;

import java.io.File;
import java.util.List;

public interface BpmnDocument {
    List<Node> getFirstLevelElements();

    Node getElement(String id);

    void setScript(String id, Integer scriptIndex, String content);

    void writeToFile(File file);
}
