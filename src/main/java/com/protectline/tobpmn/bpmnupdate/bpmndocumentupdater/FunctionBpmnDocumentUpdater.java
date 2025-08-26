package com.protectline.tobpmn.bpmnupdate.bpmndocumentupdater;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.jsproject.model.block.Block;
import com.protectline.jsproject.model.block.FunctionBlock;
import com.protectline.tobpmn.bpmnupdate.fromblock.FromBlock;
import com.protectline.tobpmn.bpmnupdate.fromblock.FromBlockFactory;

public class FunctionBpmnDocumentUpdater implements BpmnDocumentUpdater {

    FunctionBlock block;

    public FunctionBpmnDocumentUpdater(Block block) {
        this.block = (FunctionBlock) block;
    }

    @Override
    public void updateDocument(BpmnDocument document) {
        FromBlock fromBlock = FromBlockFactory.getFromBlock(block);
        fromBlock.updateDocument(document);
    }
}
