package com.protectline.bpmninjs.functionfactory.fromblock;

import com.protectline.bpmninjs.application.tobpmn.blockstobpmn.BpmUpdater;
import com.protectline.bpmninjs.bpmndocument.model.NodeType;
import com.protectline.bpmninjs.common.block.Block;

public class FromFuntionBlockFactory {

    public static BpmUpdater getFromBlock(Block block) {
        NodeType type = block.getNodeType();
        switch (type) {
            case START:
                return new UpdateStartFromBlock(block);
            case SCRIPT:
                return new UpdateScriptFromBlock(block);
            case SERVICE_TASK:
                return new UpdateServiceTaskFromBlock(block);
            default:
                throw new IllegalArgumentException("No bpmn updater found for type : " + type);
        }
    }
}
