package com.protectline.bpmninjs.translateunitfactory.function.fromblocktobpmn.documentUpdater;

import com.protectline.bpmninjs.application.tobpmn.blockstobpmn.DocumentUpdater;
import com.protectline.bpmninjs.bpmndocument.BpmnDocument;
import com.protectline.bpmninjs.common.block.Block;

public class UpdateServiceTaskFromBlock implements DocumentUpdater {
    private final Block block;

    public UpdateServiceTaskFromBlock(Block block) {
        this.block = block;
    }

    @Override
    public void updateDocument(BpmnDocument document) {
        document.setScript(block.getPath().getId(), block.getScriptIndex(), block.getContent());
    }
}
