package com.protectline.application.tobpmn;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.camundbpmnaparser.BpmnCamundaDocument;
import com.protectline.common.block.Block;
import com.protectline.jsproject.JsProject;
import com.protectline.translate.fromblocktobpmn.bpmndocumentupdater.BpmnDocumentUpdater;

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

        var bpmnDocumentUpdater = new BpmnDocumentUpdater(blocks);
        bpmnDocumentUpdater.updateDocument(document);

        String fileName = "input/" + process + ".bpmn";
        document.writeToFile(workingDirectory.resolve(fileName).toFile());
    }
}
