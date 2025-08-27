package com.protectline.common.block.jsonblock;

import com.protectline.bpmndocument.model.BpmnPath;
import com.protectline.bpmndocument.model.NodeType;
import com.protectline.common.block.BlockType;
import com.protectline.common.block.FunctionBlock;


public class FunctionJsonBlock {
    private final String name;
    private final BpmnPath path;
    private final BlockType type;
    private final NodeType nodeType;
    private final String id;
    private final String content;
    private final Integer scriptIndex;

    public FunctionJsonBlock(FunctionBlock block){
        this.name = block.getName();
        this.path = block.getPath();
        this.type = block.getType();
        this.nodeType = block.getNodeType();
        this.id = block.getId();
        this.content = block.getContent();
        this.scriptIndex = block.getScriptIndex();
    }

    public FunctionBlock toFunctionBlock(){
        return new FunctionBlock(path,name,content, nodeType, id);
    }
}
