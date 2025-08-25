package com.protectline.bpmndocument;

import com.protectline.bpmndocument.model.element.Element;

import java.nio.file.Path;
import java.util.List;

public class BpmnDocument {
    private final Path workingDirectory;
    private final String process;

    public BpmnDocument(Path workingDirectory, String process) {
        this.workingDirectory = workingDirectory;
        this.process = process;
    }

    public void updateBpmn(List<Element> elements) {

    }
}
