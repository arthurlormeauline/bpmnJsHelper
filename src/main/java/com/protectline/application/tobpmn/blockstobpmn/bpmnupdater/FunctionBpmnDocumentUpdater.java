package com.protectline.application.tobpmn.blockstobpmn.bpmnupdater;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.application.tobpmn.blockstobpmn.BpmUpdater;
import com.protectline.common.block.Block;
import com.protectline.common.block.FunctionBlock;
import com.protectline.application.tobpmn.blockstobpmn.bpmnupdater.fromblock.FromFuntionBlockFactory;

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
