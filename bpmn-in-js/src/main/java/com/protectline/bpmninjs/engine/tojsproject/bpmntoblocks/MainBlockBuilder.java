package com.protectline.bpmninjs.engine.tojsproject.bpmntoblocks;

import com.protectline.bpmninjs.engine.tojsproject.spi.BlockBuilder;
import com.protectline.bpmninjs.model.bpmndocument.api.BpmnDocument;
import com.protectline.bpmninjs.model.common.block.Block;

import java.util.ArrayList;
import java.util.List;

public class MainBlockBuilder implements BlockBuilder {
    
    private List<BlockBuilder> builders;
    
    public MainBlockBuilder() {
        builders = new ArrayList<>();
    }

    @Override
    public List<Block> getBlocks(BpmnDocument bpmnCamundaDocument) {
        List<Block> blocks = new ArrayList<>();

        for (BlockBuilder builder : builders){
            blocks.addAll(builder.getBlocks(bpmnCamundaDocument));
        }

        return blocks;
    }

    public MainBlockBuilder registerSubBlockBuilders(List<BlockBuilder> builders) {
        this.builders.addAll(builders);
        return this;
    }
}
