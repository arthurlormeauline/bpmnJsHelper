package com.protectline.tojsproject.functionblock;

import com.protectline.bpmndocument.model.NodeType;
import com.protectline.tojsproject.NodeToBlock;
import org.w3c.dom.Node;

import static com.protectline.bpmndocument.model.NodeType.getType;

public class ToFunctionBlockFactory {

    public static NodeToBlock getNodeToFunctionBlock(Node element) {
        NodeType type = getType(element);
        switch (type) {
            case SCRIPT:
                return new ScriptNodeToFunctionBlock();
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }
}
