package com.protectline.tobpmn;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.jsproject.model.block.Block;

import java.util.List;

public class MainBpmnDocumentUpdater implements BpmnDocumentUpdater {

    List<Block> blocks;

    public MainBpmnDocumentUpdater(List<Block> blocks) {
        this.blocks = blocks;
    }

    @Override
    public void updateDocument(BpmnDocument bpmnDocument) {
        for (Block block : blocks){
            BpmnDocumentUpdater updater = BpmnDocumentUpdaterFactory.getupdater(block);
            updater.updateDocument(bpmnDocument);
        }
    }
}
