package com.protectline.bpmninjs.application.tobpmn.blockstobpmn;

import com.protectline.bpmninjs.application.tobpmn.blockstobpmn.bpmnupdater.BpmnDocumentUpdater;
import com.protectline.bpmninjs.bpmndocument.BpmnDocument;
import com.protectline.bpmninjs.camundbpmnaparser.BpmnCamundaDocument;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.files.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.protectline.bpmninjs.common.block.jsonblock.FunctionJsonBlockUtil.readBlocksFromFile;

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
