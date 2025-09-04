package com.protectline.bpmninjs.application.function.tobpmn.fromblocktobpmn.documentUpdater;

import com.protectline.bpmninjs.engine.tobpmn.spi.DocumentUpdater;
import com.protectline.bpmninjs.model.bpmndocument.api.BpmnDocument;
import com.protectline.bpmninjs.model.common.block.Block;

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
