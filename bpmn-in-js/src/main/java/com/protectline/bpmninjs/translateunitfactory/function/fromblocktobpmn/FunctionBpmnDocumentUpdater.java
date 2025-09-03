package com.protectline.bpmninjs.translateunitfactory.function.fromblocktobpmn;

import com.protectline.bpmninjs.application.tobpmn.blockstobpmn.DocumentUpdater;
import com.protectline.bpmninjs.translateunitfactory.function.fromblocktobpmn.documentUpdater.FromFuntionBlockFactory;
import com.protectline.bpmninjs.bpmndocument.BpmnDocument;
import com.protectline.bpmninjs.common.block.Block;

public class FunctionBpmnDocumentUpdater implements DocumentUpdater {

    Block block;

    public FunctionBpmnDocumentUpdater(Block block) {
        this.block = (Block) block;
    }

    @Override
    public void updateDocument(BpmnDocument document) {
        DocumentUpdater fromBlock = FromFuntionBlockFactory.getFromBlock(block);
        fromBlock.updateDocument(document);
    }
}
