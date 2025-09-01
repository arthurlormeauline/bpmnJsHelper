package com.protectline.bpmninjs.application.tobpmn.blockstobpmn.bpmnupdater;

import com.protectline.bpmninjs.application.tobpmn.blockstobpmn.BpmUpdater;
import com.protectline.bpmninjs.bpmndocument.BpmnDocument;
import com.protectline.bpmninjs.common.block.Block;

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
