package com.protectline.tobpmn;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.jsproject.model.block.Block;
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
        BpmnDocument bpmnDocument = new BpmnDocument(workingDirectory, process);

        List<Block> blocks = jsProject.getBlocks();

        var mainDocumentUpdater = new MainBpmnDocumentUpdater(blocks);
        mainDocumentUpdater.updateDocument(bpmnDocument);
    }
}
