package com.protectline.bpmninjs.application.tobpmn.blockstobpmn.bpmnupdater.fromblock;

import com.protectline.bpmninjs.application.tobpmn.blockstobpmn.BpmUpdater;
import com.protectline.bpmninjs.bpmndocument.model.NodeType;
import com.protectline.bpmninjs.common.block.FunctionBlock;

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
                throw new IllegalArgumentException("No bpmn updater found for type : " + type);
        }
    }
}
