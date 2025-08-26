package com.protectline.tobpmn;

import com.protectline.tobpmn.elementbuilder.ElementBuilder;
import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.jsproject.JsProject;

import java.nio.file.Path;
import java.util.List;

public class JsProjectToBpmn {
    private final Path workingDirectory;

    public JsProjectToBpmn(Path workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public void updateBpmn(String process) {
        JsProject jsProject = new JsProject(workingDirectory, process);
        ElementBuilder elementBuilder = new ElementBuilder(jsProject);
        List<Element> elements = elementBuilder.buildElements();


        BpmnDocument bpmnDocument = new BpmnDocument(workingDirectory, process);
        bpmnDocument.updateBpmn(elements);
    }
}
