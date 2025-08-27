package com.protectline.common.block;

import com.protectline.bpmndocument.model.BpmnPath;
import com.protectline.bpmndocument.model.NodeType;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@EqualsAndHashCode
public class Block {
    protected final String name;
    protected final BpmnPath path;
    protected final BlockType type;
    protected final NodeType nodeType;
    protected String id;

    public Block(BpmnPath path, String name, BlockType type, NodeType nodeType) {
        id = UUID.randomUUID().toString();
        this.path = path;
        this.name = name;
        this.type = type;
        this.nodeType = nodeType;
    }
}
