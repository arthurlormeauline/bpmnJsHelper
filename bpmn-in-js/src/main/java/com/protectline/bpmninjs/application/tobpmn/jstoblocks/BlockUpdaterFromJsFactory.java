package com.protectline.bpmninjs.application.tobpmn.jstoblocks;

import com.protectline.bpmninjs.common.block.BlockType;

public class BlockUpdaterFromJsFactory {
    
    public static UpdateBlockFromJs getUpdater(BlockType blockType) {
        switch (blockType) {
            case FUNCTION:
                return new BlockUpdaterFromJs();
            default:
                throw new IllegalArgumentException("Unsupported block type: " + blockType);
        }
    }
}
