package com.protectline.application.tobpmn.blockstobpmn;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.camundbpmnaparser.BpmnCamundaDocument;
import com.protectline.common.block.Block;
import com.protectline.application.tobpmn.blockstobpmn.bpmnupdater.BpmnDocumentUpdater;
import com.protectline.files.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static com.protectline.common.block.jsonblock.FunctionJsonBlockUtil.readBlocksFromFile;

public class FromBlockToBpmn {

    private final FileUtil fileUtil;

    public FromBlockToBpmn(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    public void updateBpmnFromBlocks(String process) throws IOException {
        File bpmnFile = fileUtil.getBpmnFile(process).toFile();
        BpmnDocument document = new BpmnCamundaDocument(bpmnFile);

        List<Block> blocks = readBlocksFromFile(fileUtil.getBlocksFile(process));

        var bpmnDocumentUpdater = new BpmnDocumentUpdater(blocks);
        bpmnDocumentUpdater.updateDocument(document);

        document.writeToFile(bpmnFile);
    }

}
