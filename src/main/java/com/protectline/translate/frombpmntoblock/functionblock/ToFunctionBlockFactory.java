package com.protectline.translate.frombpmntoblock.functionblock;

import com.protectline.bpmndocument.model.Node;
import com.protectline.bpmndocument.model.NodeType;
import com.protectline.translate.frombpmntoblock.NodeToBlock;
import com.protectline.translate.frombpmntoblock.functionblock.toblock.ScriptNodeToFunctionBlock;
import com.protectline.translate.frombpmntoblock.functionblock.toblock.ServiceTaskNodeToFunctionBlock;
import com.protectline.translate.frombpmntoblock.functionblock.toblock.StartNodeToFunctionBlock;

import static com.protectline.bpmndocument.model.NodeType.getType;

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
