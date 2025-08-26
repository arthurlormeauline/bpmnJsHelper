package com.protectline.bpmndocument;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.w3c.dom.Node;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class BpmnDocument {
    private BpmnModelInstance modelInstance = null;

    public BpmnDocument(Path workingDirectory, String processName) {
        File processFile = workingDirectory.resolve("input").resolve(processName + ".bpmn").toFile();
        this.modelInstance = Bpmn.readModelFromFile(processFile);
    }

    public List<Node> getFirstLevelElements() {
        return null;
    }
}
