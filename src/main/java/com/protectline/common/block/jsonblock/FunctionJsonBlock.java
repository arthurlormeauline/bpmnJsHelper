package com.protectline.common.block.jsonblock;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.protectline.bpmndocument.model.BpmnPath;
import com.protectline.bpmndocument.model.NodeType;
import com.protectline.common.block.BlockType;
import com.protectline.common.block.FunctionBlock;
import lombok.Getter;

@Getter
public class FunctionJsonBlock {
    @JsonProperty("name")
    private final String name;
    @JsonProperty("path")
    private final BpmnPath path;
    @JsonProperty("type")
    private final BlockType type;
    @JsonProperty("nodeType")
    private final NodeType nodeType;
    @JsonProperty("id")
    private final String id;
    @JsonProperty("content")
    private final String content;
    @JsonProperty("scriptIndex")
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

    @JsonCreator
    public FunctionJsonBlock(
            @JsonProperty("name") String name,
            @JsonProperty("path") BpmnPath path,
            @JsonProperty("type") BlockType type,
            @JsonProperty("nodeType") NodeType nodeType,
            @JsonProperty("id") String id,
            @JsonProperty("content") String content,
            @JsonProperty("scriptIndex") Integer scriptIndex) {
        this.name = name;
        this.path = path;
        this.type = type;
        this.nodeType = nodeType;
        this.id = id;
        this.content = content;
        this.scriptIndex = scriptIndex;
    }

    public FunctionBlock toFunctionBlock(){
        return new FunctionBlock(path,name,content, nodeType, id);
    }


}
