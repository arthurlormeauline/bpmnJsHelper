package com.protectline.tobpmn.bpmnupdate;

import com.protectline.bpmndocument.model.camundaadapter.BpmnCamundaDocument;
import com.protectline.jsproject.model.block.FunctionBlock;

public class FunctionBpmnDocumentUpdater implements BpmnDocumentUpdater {

    FunctionBlock block;

    public FunctionBpmnDocumentUpdater(FunctionBlock block){
        this.block=block;
    }

    @Override
    public void updateDocument(BpmnCamundaDocument bpmnCamundaDocument) {
    }
}
