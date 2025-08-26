package com.protectline.tobpmn;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.jsproject.model.block.FunctionBlock;

public class FunctionBpmnDocumentUpdater implements BpmnDocumentUpdater {

    FunctionBlock block;

    public FunctionBpmnDocumentUpdater(FunctionBlock block){
        this.block=block;
    }

    @Override
    public void updateDocument(BpmnDocument bpmnDocument) {
    }
}
