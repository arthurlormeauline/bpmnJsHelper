package com.protectline.tojsproject.block;

import com.protectline.bpmndocument.model.camundaadapter.BpmnCamundaDocument;
import com.protectline.jsproject.model.block.Block;

import java.util.ArrayList;
import java.util.List;

public class MainBlockBuilder implements BlockBuilder {
    
    private List<BlockBuilder> builders;
    
    public MainBlockBuilder() {
        builders = new ArrayList<>();
    }

    @Override
    public List<Block> getBlocks(BpmnCamundaDocument bpmnCamundaDocument) {
        List<Block> blocks = new ArrayList<>();

        for (BlockBuilder builder : builders){
            blocks.addAll(builder.getBlocks(bpmnCamundaDocument));
        }

        return blocks;
    }

    public MainBlockBuilder registerSubBlockBuilder(BlockBuilder builder) {
        builders.add(builder);
        return this;
    }

    public MainBlockBuilder registerSubBlockBuilders(List<BlockBuilder> builders) {
        this.builders.addAll(builders);
        return this;
    }
}
