package com.protectline.bpmninjs.functionfactory.functionblock;

import com.protectline.bpmninjs.functionfactory.functionblock.toblock.ScriptNodeToFunctionBlock;
import com.protectline.bpmninjs.functionfactory.functionblock.toblock.ServiceTaskNodeToFunctionBlock;
import com.protectline.bpmninjs.functionfactory.functionblock.toblock.StartNodeToFunctionBlock;
import com.protectline.bpmninjs.bpmndocument.model.Node;
import com.protectline.bpmninjs.bpmndocument.model.NodeType;

import static com.protectline.bpmninjs.bpmndocument.model.NodeType.getType;

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
