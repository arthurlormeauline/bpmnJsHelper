package com.protectline.translate.fromblocktobpmn.fromblock;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.translate.fromblocktobpmn.BpmUpdater;
import com.protectline.common.block.FunctionBlock;

public class UpdateServiceTaskFromFunctionBlock implements BpmUpdater {
    private final FunctionBlock block;

    public UpdateServiceTaskFromFunctionBlock(FunctionBlock block) {
        this.block = block;
    }

    @Override
    public void updateDocument(BpmnDocument document) {
        document.setScript(block.getPath().getId(), block.getScriptIndex(), block.getContent());
    }
}
