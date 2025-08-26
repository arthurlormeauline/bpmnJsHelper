package com.protectline.tobpmn.bpmnupdate.fromblock;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.jsproject.model.block.Block;

public class UpdateStartFromBlock implements FromBlock {

    private final Block block;

    public UpdateStartFromBlock(Block block) {
        this.block = block;
    }

    @Override
    public void updateDocument(BpmnDocument document) {

    }
}
