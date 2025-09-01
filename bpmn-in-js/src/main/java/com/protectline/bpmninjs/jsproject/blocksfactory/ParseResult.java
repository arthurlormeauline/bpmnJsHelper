package com.protectline.bpmninjs.jsproject.blocksfactory;

import com.protectline.bpmninjs.common.block.Block;

import java.util.List;

class ParseResult {
    private final List<Block> blocks;
    private final String cleanedContent;

    public ParseResult(List<Block> blocks, String cleanedContent) {
        this.blocks = blocks;
        this.cleanedContent = cleanedContent;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public String getCleanedContent() {
        return cleanedContent;
    }
}

