package com.protectline.tobpmn;

import com.protectline.jsproject.model.block.Block;
import com.protectline.jsproject.model.block.BlockType;
import com.protectline.jsproject.model.block.FunctionBlock;

public class BpmnDocumentUpdaterFactory {
    public static BpmnDocumentUpdater getupdater(Block block) {
        BlockType type = block.getType();
        switch (type) {
            case FUNCTION:
                return new FunctionBpmnDocumentUpdater((FunctionBlock) block);
            default:
                throw new IllegalArgumentException("No Bpmn document updater for this type of block : " + type);
        }
    }
}
