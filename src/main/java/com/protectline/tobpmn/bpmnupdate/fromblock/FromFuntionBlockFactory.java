package com.protectline.tobpmn.bpmnupdate.fromblock;

import com.protectline.bpmndocument.model.NodeType;
import com.protectline.jsproject.model.block.Block;
import com.protectline.jsproject.model.block.FunctionBlock;

public class FromFuntionBlockFactory {

    public static FromFunctionBlock getFromBlock(FunctionBlock block) {
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
