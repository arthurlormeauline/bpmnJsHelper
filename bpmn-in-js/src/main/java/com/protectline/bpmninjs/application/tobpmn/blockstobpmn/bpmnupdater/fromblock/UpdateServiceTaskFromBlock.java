package com.protectline.bpmninjs.application.tobpmn.blockstobpmn.bpmnupdater.fromblock;

import com.protectline.bpmninjs.application.tobpmn.blockstobpmn.BpmUpdater;
import com.protectline.bpmninjs.bpmndocument.BpmnDocument;
import com.protectline.bpmninjs.common.block.Block;

public class UpdateServiceTaskFromBlock implements BpmUpdater {
    private final Block block;

    public UpdateServiceTaskFromBlock(Block block) {
        this.block = block;
    }

    @Override
    public void updateDocument(BpmnDocument document) {
        document.setScript(block.getPath().getId(), block.getScriptIndex(), block.getContent());
    }
}
