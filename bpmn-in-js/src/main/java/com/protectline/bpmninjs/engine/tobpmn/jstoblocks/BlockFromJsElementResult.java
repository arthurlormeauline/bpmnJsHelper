package com.protectline.bpmninjs.engine.tobpmn.jstoblocks;

import com.protectline.bpmninjs.model.block.Block;

import java.util.List;

public class BlockFromJsElementResult {
    private final List<Block> blocks;
    private final String cleanedContent;

    public BlockFromJsElementResult(List<Block> blocks, String cleanedContent) {
        this.blocks = blocks;
        this.cleanedContent = cleanedContent;
    }

    public List<Block> getBlocks() {
        return blocks;
    }
}

