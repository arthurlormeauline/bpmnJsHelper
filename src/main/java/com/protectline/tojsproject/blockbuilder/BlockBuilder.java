package com.protectline.tojsproject.blockbuilder;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.bpmndocument.model.block.Block;

import java.util.List;

public class BlockBuilder {
    
    private final BpmnDocument bpmnDocument;
    
    public BlockBuilder(BpmnDocument bpmnDocument) {
        this.bpmnDocument = bpmnDocument;
    }
    
    public List<Block> buildBlocks() {
        return List.of();
    }
}
