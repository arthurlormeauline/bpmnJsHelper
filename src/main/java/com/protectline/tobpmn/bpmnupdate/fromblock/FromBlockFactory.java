package com.protectline.tobpmn.bpmnupdate.fromblock;

import com.protectline.bpmndocument.model.NodeType;
import com.protectline.jsproject.model.block.Block;

public class FromBlockFactory {

    public static FromBlock getFromBlock(Block block) {
        NodeType type = block.getNodeType();
        switch (type) {
            case START:
                return new UpdateStartFromBlock(block);
            case SCRIPT:
                return new UpdateScriptFromBlock(block);
            case SERVICE_TASK:
                return new UpdateServiceTaskFromBlock(block);
            default:
                throw new IllegalArgumentException("Could not update document from node type : " + type);
        }
    }
}
