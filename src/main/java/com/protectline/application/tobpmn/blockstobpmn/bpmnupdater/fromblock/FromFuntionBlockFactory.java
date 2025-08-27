package com.protectline.application.tobpmn.blockstobpmn.bpmnupdater.fromblock;

import com.protectline.bpmndocument.model.NodeType;
import com.protectline.application.tobpmn.blockstobpmn.BpmUpdater;
import com.protectline.common.block.FunctionBlock;

public class FromFuntionBlockFactory {

    public static BpmUpdater getFromBlock(FunctionBlock block) {
        NodeType type = block.getNodeType();
        switch (type) {
            case START:
                return new UpdateStartFromFunctionBlock(block);
            case SCRIPT:
                return new UpdateScriptFromFunctionBlock(block);
            case SERVICE_TASK:
                return new UpdateServiceTaskFromFunctionBlock(block);
            default:
                throw new IllegalArgumentException("Could not update document from node type : " + type);
        }
    }
}
