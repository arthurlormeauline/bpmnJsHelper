package com.protectline.tojsproject.blockbuilder;

import com.protectline.bpmndocument.model.camundaadapter.BpmnCamundaDocument;
import com.protectline.jsproject.model.block.Block;

import java.util.List;

public class BlockBuilder {
    
    private final BpmnCamundaDocument bpmnCamundaDocument;
    
    public BlockBuilder(BpmnCamundaDocument bpmnCamundaDocument) {
        this.bpmnCamundaDocument = bpmnCamundaDocument;
    }
    
    public List<Block> buildBlocks() {
        return List.of();
    }
}
