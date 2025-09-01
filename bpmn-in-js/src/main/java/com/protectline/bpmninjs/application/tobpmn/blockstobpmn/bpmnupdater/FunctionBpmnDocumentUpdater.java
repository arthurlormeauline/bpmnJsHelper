package com.protectline.bpmninjs.application.tobpmn.blockstobpmn.bpmnupdater;

import com.protectline.bpmninjs.application.tobpmn.blockstobpmn.BpmUpdater;
import com.protectline.bpmninjs.application.tobpmn.blockstobpmn.bpmnupdater.fromblock.FromFuntionBlockFactory;
import com.protectline.bpmninjs.bpmndocument.BpmnDocument;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.common.block.FunctionBlock;

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
