package com.protectline.tojsproject.stub;

import com.protectline.bpmndocument.model.BpmnPath;
import com.protectline.bpmndocument.model.NodeType;
import com.protectline.jsproject.model.block.FunctionBlock;

public class StubBlock {
    public static FunctionBlock getExpectedBlock(String id, String name, String script, NodeType nodeType) {
        BpmnPath expectedPath = new BpmnPath(id);
        String expectedName = name;
        String expectedContent = script;
        return new FunctionBlock(expectedPath, expectedName, expectedContent, nodeType);
    }

}
