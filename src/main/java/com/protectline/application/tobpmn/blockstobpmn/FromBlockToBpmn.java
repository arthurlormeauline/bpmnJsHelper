package com.protectline.application.tobpmn.blockstobpmn;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.camundbpmnaparser.BpmnCamundaDocument;
import com.protectline.common.block.Block;
import com.protectline.application.tobpmn.blockstobpmn.bpmnupdater.BpmnDocumentUpdater;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class FromBlockToBpmn {

    private final Path workingDirectory;

    public FromBlockToBpmn(Path workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public void updateBpmnFromBlocks(Path workingDirectory, String process) throws IOException {
        BpmnDocument document = new BpmnCamundaDocument(workingDirectory, process);

        // get blocks from file
        List<Block> blocks = null;

        var bpmnDocumentUpdater = new BpmnDocumentUpdater(blocks);
        bpmnDocumentUpdater.updateDocument(document);

        String fileName = "input/" + process + ".bpmn";
        document.writeToFile(workingDirectory.resolve(fileName).toFile());
    }
}
