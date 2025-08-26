package com.protectline.tojsproject;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.jsproject.model.block.Block;

import java.util.ArrayList;
import java.util.List;

public class MainBlockBuilder implements BlockBuilder {
    
    private List<BlockBuilder> builders;
    
    public MainBlockBuilder() {
        builders = new ArrayList<>();
    }

    @Override
    public List<Block> getBlocks(BpmnDocument bpmnDocument) {
        List<Block> blocks = new ArrayList<>();

        for (BlockBuilder builder : builders){
            blocks.addAll(builder.getBlocks(bpmnDocument));
        }

        return blocks;
    }

    public MainBlockBuilder registerSubBlockBuilder(BlockBuilder builder) {
        builders.add(builder);
        return this;
    }

    public MainBlockBuilder registerSubBlockBuilders(List<BlockBuilder> builders) {
        builders.addAll(builders);
        return this;
    }
}
