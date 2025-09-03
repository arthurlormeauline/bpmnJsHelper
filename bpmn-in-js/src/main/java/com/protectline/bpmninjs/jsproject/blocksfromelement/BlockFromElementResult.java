package com.protectline.bpmninjs.jsproject.blocksfromelement;

import com.protectline.bpmninjs.common.block.Block;

import java.util.List;

public class BlockFromElementResult {
    private final List<Block> blocks;
    private final String cleanedContent;

    public BlockFromElementResult(List<Block> blocks, String cleanedContent) {
        this.blocks = blocks;
        this.cleanedContent = cleanedContent;
    }

    public List<Block> getBlocks() {
        return blocks;
    }
}

