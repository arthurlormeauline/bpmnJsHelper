package com.protectline.bpmndocument;

import com.protectline.bpmndocument.model.element.Element;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BpmnDocument {
    private BpmnModelInstance modelInstance = null;

    public BpmnDocument(Path workingDirectory, String processName) {
        File processFile = workingDirectory.resolve("input").resolve(processName + ".bpmn").toFile();
        this.modelInstance = Bpmn.readModelFromFile(processFile);
    }

    public void updateBpmn(List<Element> elements) {
    }

    public List<Element> getAllScripts() {
        List<Element> scriptElements = new ArrayList<>();

        var document = modelInstance.getDocument();

        return scriptElements;
    }
}
