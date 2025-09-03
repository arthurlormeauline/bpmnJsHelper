package com.protectline.bpmninjs.application.tobpmn.blockstobpmn;

import com.protectline.bpmninjs.bpmndocument.BpmnDocument;
import com.protectline.bpmninjs.camundbpmnaparser.BpmnCamundaDocument;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.files.FileUtil;
import com.protectline.bpmninjs.application.mainfactory.MainFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.protectline.bpmninjs.common.block.jsonblock.FunctionJsonBlockUtil.readBlocksFromFile;

public class FromBlockToBpmn {

    private final FileUtil fileUtil;
    private final MainFactory mainFactory;

    public FromBlockToBpmn(FileUtil fileUtil, MainFactory mainFactory) {
        this.fileUtil = fileUtil;
        this.mainFactory = mainFactory;
    }

    public void updateBpmnFromBlocks(String process) throws IOException {
        File bpmnFile = fileUtil.getBpmnFile(process).toFile();
        BpmnDocument document = new BpmnCamundaDocument(bpmnFile);

        List<Block> blocks = readBlocksFromFile(fileUtil.getBlocksFile(process));

        // Itérer sur les blocks et déléguer à MainFactory
        for (Block block : blocks) {
            BpmUpdater updater = mainFactory.getBpmnUpdater(block);
            updater.updateDocument(document);
        }

        document.writeToFile(bpmnFile);
    }

}
