package com.protectline.tojsproject.block.functionblock;

import com.protectline.bpmndocument.model.Node;
import com.protectline.bpmndocument.model.NodeType;
import com.protectline.tojsproject.block.NodeToBlock;
import com.protectline.tojsproject.block.functionblock.toblock.ScriptNodeToFunctionBlock;
import com.protectline.tojsproject.block.functionblock.toblock.ServiceTaskNodeToFunctionBlock;
import com.protectline.tojsproject.block.functionblock.toblock.StartNodeToFunctionBlock;

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
