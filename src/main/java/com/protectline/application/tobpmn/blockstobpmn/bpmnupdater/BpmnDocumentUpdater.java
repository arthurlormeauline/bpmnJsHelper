package com.protectline.application.tobpmn.blockstobpmn.bpmnupdater;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.application.tobpmn.blockstobpmn.BpmUpdater;
import com.protectline.common.block.Block;

import java.util.List;

public class BpmnDocumentUpdater implements BpmUpdater {

    List<Block> blocks;

    public BpmnDocumentUpdater(List<Block> blocks) {
        this.blocks = blocks;
    }

    @Override
    public void updateDocument(BpmnDocument document) {
        if (blocks != null) {
            for (Block block : blocks) {
                BpmUpdater updater = BpmnDocumentUpdaterFactory
                        .getupdater(block);
                updater.updateDocument(document);
            }
        }
    }
}
