package com.protectline.bpmninjs.translateunitfactory.function.tobpmn.fromblocktobpmn.documentUpdater;

import com.protectline.bpmninjs.application.tobpmn.spi.DocumentUpdater;
import com.protectline.bpmninjs.bpmndocument.BpmnDocument;
import com.protectline.bpmninjs.common.block.Block;

public class UpdateScriptFromBlock implements DocumentUpdater {

    private final Block block;

    public UpdateScriptFromBlock(Block block) {
        this.block = block;
    }

    @Override
    public void updateDocument(BpmnDocument document) {
        document.setScript(block.getPath().getId(), block.getScriptIndex(), block.getContent());
    }
}
