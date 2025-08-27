package com.protectline.tojsproject.blockbuilder;

import com.protectline.camundbpmnaparser.BpmnCamundaDocument;
import com.protectline.common.block.Block;

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
