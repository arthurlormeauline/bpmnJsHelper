package com.protectline.jsproject.model.block;

import com.protectline.bpmndocument.model.BpmnPath;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Block {
    private final String name;
    private final BpmnPath path;
    private final BlockType type;

    public Block(BpmnPath path, String name, BlockType type) {
        this.path = path;
        this.name = name;
        this.type = type;
    }
}
