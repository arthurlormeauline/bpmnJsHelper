package com.protectline.bpmninjs.bpmndocument;


import com.protectline.bpmninjs.bpmndocument.model.Node;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface BpmnDocument {
    List<Node> getFirstLevelElements();

    void setScript(String elementId, Integer scriptIndex, String content);

    void writeToFile(File file) throws IOException;
}
