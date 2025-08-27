package com.protectline.application.tobpmn.blockstobpmn.bpmnupdater;

import com.protectline.application.tobpmn.blockstobpmn.BpmUpdater;
import com.protectline.common.block.Block;
import com.protectline.common.block.BlockType;

public class BpmnDocumentUpdaterFactory {
    public static BpmUpdater getupdater(Block block) {
        BlockType type = block.getType();
        switch (type) {
            case FUNCTION:
                return new FunctionBpmnDocumentUpdater(block);
            default:
                throw new IllegalArgumentException("No Bpmn document updater for this type of block : " + type);
        }
    }
}
