package com.protectline.bpmninjs.common.block.jsonblock;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.protectline.bpmninjs.bpmndocument.model.NodeType;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.common.block.BlockType;
import lombok.Getter;

import java.util.Map;

@Getter
public class JsonBlock {
    @JsonProperty("id")
    private final String id;
    @JsonProperty("path")
    private final BpmnPathJson path;
    @JsonProperty("attributes")
    private final Map<String, String> attributes;

    public JsonBlock(Block block){
        this.id = block.getId();
        this.path = new BpmnPathJson(block.getPath());
        this.attributes = block.getAttributes();
    }

    @JsonCreator
    public JsonBlock(
            @JsonProperty("id") String id,
            @JsonProperty("path") BpmnPathJson path,
            @JsonProperty("attributes") Map<String, String> attributes) {
        this.id = id;
        this.path = path;
        this.attributes = attributes;
    }

    public Block toBlock(){
        Block block = new Block(path.toBpmnPath(), 
                               attributes.get("name"), 
                               BlockType.valueOf(attributes.get("type")), 
                               NodeType.valueOf(attributes.get("nodeType")), 
                               id);
        // Set all attributes
        if (attributes != null) {
            for (Map.Entry<String, String> entry : attributes.entrySet()) {
                block.setAttribute(entry.getKey(), entry.getValue());
            }
        }
        return block;
    }

    // Backward compatibility methods
    public Block toFunctionBlock(){
        return toBlock();
    }
    
    // Backward compatibility getters for tests
    public String getName() {
        return attributes.get("name");
    }
    
    public String getContent() {
        return attributes.get("content");
    }
    
    public BlockType getType() {
        String typeStr = attributes.get("type");
        return typeStr != null ? BlockType.valueOf(typeStr) : null;
    }
    
    public NodeType getNodeType() {
        String nodeTypeStr = attributes.get("nodeType");
        return nodeTypeStr != null ? NodeType.valueOf(nodeTypeStr) : null;
    }
    
    public Integer getScriptIndex() {
        String scriptIndexStr = attributes.get("scriptIndex");
        return scriptIndexStr != null ? Integer.valueOf(scriptIndexStr) : null;
    }
}
