package com.protectline.jsproject.model.block;

import com.protectline.bpmndocument.model.BpmnPath;
import com.protectline.bpmndocument.model.NodeType;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Block {
    private final String name;
    private final BpmnPath path;
    private final BlockType type;
    private final NodeType nodeType;

    public Block(BpmnPath path, String name, BlockType type, NodeType nodeType) {
        this.path = path;
        this.name = name;
        this.type = type;
        this.nodeType = nodeType;
    }
}
