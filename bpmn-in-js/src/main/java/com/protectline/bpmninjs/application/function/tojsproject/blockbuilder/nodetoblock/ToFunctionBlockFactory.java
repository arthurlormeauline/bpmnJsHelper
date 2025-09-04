package com.protectline.bpmninjs.application.function.tojsproject.blockbuilder.nodetoblock;

import com.protectline.bpmninjs.application.function.tojsproject.blockbuilder.nodetoblock.impl.ScriptNodeToFunctionBlock;
import com.protectline.bpmninjs.application.function.tojsproject.blockbuilder.nodetoblock.impl.ServiceTaskNodeToFunctionBlock;
import com.protectline.bpmninjs.application.function.tojsproject.blockbuilder.nodetoblock.impl.StartNodeToFunctionBlock;
import com.protectline.bpmninjs.model.bpmndocument.api.model.Node;
import com.protectline.bpmninjs.model.bpmndocument.api.model.NodeType;

import static com.protectline.bpmninjs.model.bpmndocument.api.model.NodeType.getType;

public class ToFunctionBlockFactory {

    public static NodeToBlock getNodeToFunctionBlock(Node node) {
        NodeType type = getType(node);
        switch (type) {
            case SCRIPT:
                return new ScriptNodeToFunctionBlock(node);
            case SERVICE_TASK:
                return new ServiceTaskNodeToFunctionBlock(node);
            case START:
                return new StartNodeToFunctionBlock(node);
            default:

                throw new IllegalStateException("Unexpected value: " + type);
        }
    }
}
