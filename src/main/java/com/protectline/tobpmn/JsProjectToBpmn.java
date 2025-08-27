package com.protectline.tobpmn;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.bpmndocument.model.camundaadapter.BpmnCamundaDocument;
import com.protectline.jsproject.model.block.Block;
import com.protectline.jsproject.JsProject;
import com.protectline.tobpmn.bpmnupdate.bpmndocumentupdater.MainBpmnDocumentUpdater;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class JsProjectToBpmn {
    private final Path workingDirectory;

    public JsProjectToBpmn(Path workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public void updateBpmn(String process) throws IOException {
        JsProject jsProject = new JsProject(workingDirectory, process);
        BpmnDocument document = new BpmnCamundaDocument(workingDirectory, process);

        List<Block> blocks = jsProject.getBlocks();

        var mainDocumentUpdater = new MainBpmnDocumentUpdater(blocks);
        mainDocumentUpdater.updateDocument(document);
        document.writeToFile(workingDirectory.resolve("input/"+process+".bpmn").toFile());
    }
}
