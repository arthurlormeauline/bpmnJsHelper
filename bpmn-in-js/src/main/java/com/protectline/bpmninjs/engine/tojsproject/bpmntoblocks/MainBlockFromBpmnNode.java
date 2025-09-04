package com.protectline.bpmninjs.engine.tojsproject.bpmntoblocks;

import com.protectline.bpmninjs.engine.tojsproject.spi.BlockFromBpmnNode;
import com.protectline.bpmninjs.model.bpmndocument.api.BpmnDocument;
import com.protectline.bpmninjs.model.block.Block;

import java.util.ArrayList;
import java.util.List;

public class MainBlockFromBpmnNode implements BlockFromBpmnNode {
    
    private List<BlockFromBpmnNode> builders;
    
    public MainBlockFromBpmnNode() {
        builders = new ArrayList<>();
    }

    @Override
    public List<Block> getBlocks(BpmnDocument document) {
        List<Block> blocks = new ArrayList<>();

        for (BlockFromBpmnNode builder : builders){
            blocks.addAll(builder.getBlocks(document));
        }

        return blocks;
    }

    public MainBlockFromBpmnNode registerSubBlockBuilders(List<BlockFromBpmnNode> builders) {
        this.builders.addAll(builders);
        return this;
    }
}
