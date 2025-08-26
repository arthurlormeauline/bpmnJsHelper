package com.protectline.jsproject.model.block;

import com.protectline.bpmndocument.model.BpmnPath;
import com.protectline.bpmndocument.model.NodeType;
import lombok.EqualsAndHashCode;

import static com.protectline.jsproject.model.block.BlockType.FUNCTION;

@EqualsAndHashCode
public class FunctionBlock extends Block {
    String content;

    public FunctionBlock(BpmnPath path, String name, String content, NodeType nodeType) {
        super(path, name, FUNCTION, nodeType);
        this.content = content;
    }
}
