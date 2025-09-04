package com.protectline.bpmninjs.engine.tobpmn.blockstobpmn;

import com.protectline.bpmninjs.engine.tobpmn.spi.BpmnDocumentUpdater;
import com.protectline.bpmninjs.model.bpmndocument.api.BpmnDocument;
import com.protectline.bpmninjs.model.bpmndocument.camundaimpl.BpmnCamundaDocument;
import com.protectline.bpmninjs.model.block.Block;
import com.protectline.bpmninjs.engine.files.FileService;
import com.protectline.bpmninjs.engine.mainfactory.MainFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.protectline.bpmninjs.model.block.persist.BlockUtil.readBlocksFromFile;


public class FromBlockToBpmn {

    private final FileService fileService;
    private final MainFactory mainFactory;

    public FromBlockToBpmn(FileService fileService, MainFactory mainFactory) {
        this.fileService = fileService;
        this.mainFactory = mainFactory;
    }

    public void updateBpmnFromBlocks(String process) throws IOException {
        File bpmnFile = fileService.getBpmnFile(process).toFile();
        BpmnDocument document = new BpmnCamundaDocument(bpmnFile);

        List<Block> blocks = readBlocksFromFile(fileService.getBlocksFile(process));

        // Itérer sur les blocks et déléguer à MainFactory
        for (Block block : blocks) {
            BpmnDocumentUpdater updater = mainFactory.getBpmnUpdater(block);
            updater.updateDocument(document);
        }

        document.writeToFile(bpmnFile);
    }

}
