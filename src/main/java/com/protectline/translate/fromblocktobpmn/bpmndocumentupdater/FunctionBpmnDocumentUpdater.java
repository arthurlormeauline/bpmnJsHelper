package com.protectline.translate.fromblocktobpmn.bpmndocumentupdater;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.translate.fromblocktobpmn.BpmUpdater;
import com.protectline.common.block.Block;
import com.protectline.common.block.FunctionBlock;
import com.protectline.translate.fromblocktobpmn.fromblock.FromFuntionBlockFactory;

public class FunctionBpmnDocumentUpdater implements BpmUpdater {

    FunctionBlock block;

    public FunctionBpmnDocumentUpdater(Block block) {
        this.block = (FunctionBlock) block;
    }

    @Override
    public void updateDocument(BpmnDocument document) {
        BpmUpdater fromFunctionBlock = FromFuntionBlockFactory.getFromBlock(block);
        fromFunctionBlock.updateDocument(document);
    }
}
