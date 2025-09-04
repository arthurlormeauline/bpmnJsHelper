package com.protectline.bpmninjs.engine.tobpmn.blockstobpmn;

import com.protectline.bpmninjs.engine.tobpmn.spi.DocumentUpdater;
import com.protectline.bpmninjs.model.bpmndocument.api.BpmnDocument;
import com.protectline.bpmninjs.model.bpmndocument.camundaimpl.BpmnCamundaDocument;
import com.protectline.bpmninjs.model.common.block.Block;
import com.protectline.bpmninjs.engine.files.FileUtil;
import com.protectline.bpmninjs.engine.mainfactory.MainFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.protectline.bpmninjs.model.common.block.persist.BlockUtil.readBlocksFromFile;


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
            DocumentUpdater updater = mainFactory.getBpmnUpdater(block);
            updater.updateDocument(document);
        }

        document.writeToFile(bpmnFile);
    }

}
