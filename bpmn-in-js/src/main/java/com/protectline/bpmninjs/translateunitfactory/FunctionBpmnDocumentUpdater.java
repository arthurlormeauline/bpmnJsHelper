package com.protectline.bpmninjs.translateunitfactory;

import com.protectline.bpmninjs.application.tobpmn.blockstobpmn.BpmUpdater;
import com.protectline.bpmninjs.functionfactory.fromblock.FromFuntionBlockFactory;
import com.protectline.bpmninjs.bpmndocument.BpmnDocument;
import com.protectline.bpmninjs.common.block.Block;

public class FunctionBpmnDocumentUpdater implements BpmUpdater {

    Block block;

    public FunctionBpmnDocumentUpdater(Block block) {
        this.block = (Block) block;
    }

    @Override
    public void updateDocument(BpmnDocument document) {
        BpmUpdater fromBlock = FromFuntionBlockFactory.getFromBlock(block);
        fromBlock.updateDocument(document);
    }
}
