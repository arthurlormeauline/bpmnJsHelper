package com.protectline.tobpmn.bpmnupdate.fromblock;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.jsproject.model.block.Block;

public class UpdateScriptFromBlock implements FromBlock {

    private final Block block;

    public UpdateScriptFromBlock(Block block) {
        this.block = block;
    }

    @Override
    public void updateDocument(BpmnDocument document) {

    }
}
