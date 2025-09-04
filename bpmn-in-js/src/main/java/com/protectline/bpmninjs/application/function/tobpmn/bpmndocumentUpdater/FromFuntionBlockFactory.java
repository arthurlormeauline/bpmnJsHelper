package com.protectline.bpmninjs.application.function.tobpmn.bpmndocumentUpdater;

import com.protectline.bpmninjs.engine.tobpmn.spi.BpmnDocumentUpdater;
import com.protectline.bpmninjs.model.bpmndocument.api.model.NodeType;
import com.protectline.bpmninjs.model.block.Block;

public class FromFuntionBlockFactory {

    public static BpmnDocumentUpdater getFromBlock(Block block) {
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
