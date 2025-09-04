package com.protectline.bpmninjs.application.function.tobpmn.bpmndocumentUpdater;

import com.protectline.bpmninjs.engine.tobpmn.spi.BpmnDocumentUpdater;
import com.protectline.bpmninjs.model.bpmndocument.api.BpmnDocument;
import com.protectline.bpmninjs.model.block.Block;

public class UpdateStartFromBlock implements BpmnDocumentUpdater {

    private final Block block;

    public UpdateStartFromBlock(Block block) {
        this.block = block;
    }

    @Override
    public void updateDocument(BpmnDocument document) {
        document.setScript(block.getPath().getId(), block.getScriptIndex(), block.getContent());
    }
}
