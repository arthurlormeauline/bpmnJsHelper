package com.protectline.tobpmn.bpmnupdate.fromblock;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.jsproject.model.block.Block;
import com.protectline.jsproject.model.block.FunctionBlock;

public class UpdateStartFromFunctionBlock implements FromFunctionBlock {

    private final FunctionBlock block;

    public UpdateStartFromFunctionBlock(FunctionBlock block) {
        this.block = block;
    }

    @Override
    public void updateDocument(BpmnDocument document) {
        document.setScript(block.getPath().getId(), block.getScriptIndex(), block.getContent());
    }
}
