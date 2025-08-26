package com.protectline.tobpmn.bpmnupdate.bpmndocumentupdater;

import com.protectline.jsproject.model.block.Block;
import com.protectline.jsproject.model.block.BlockType;

public class BpmnDocumentUpdaterFactory {
    public static BpmnDocumentUpdater getupdater(Block block) {
        BlockType type = block.getType();
        switch (type) {
            case FUNCTION:
                return new FunctionBpmnDocumentUpdater(block);
            default:
                throw new IllegalArgumentException("No Bpmn document updater for this type of block : " + type);
        }
    }
}
