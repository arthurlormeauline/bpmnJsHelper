package com.protectline.bpmninjs.common.block;

import com.protectline.bpmninjs.bpmndocument.model.BpmnPath;
import com.protectline.bpmninjs.bpmndocument.model.NodeType;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EqualsAndHashCode
public class Block {
    private final String id;
    private final BpmnPath path;
    private final Map<String, String> attributes;

    public Block(BpmnPath path, String name, BlockType type, NodeType nodeType) {
        this.id = UUID.randomUUID().toString();
        this.path = path;
        this.attributes = new HashMap<>();
        setAttribute("name", name);
        setAttribute("type", type != null ? type.toString() : null);
        setAttribute("nodeType", nodeType != null ? nodeType.toString() : null);
        // Auto-populate scriptIndex from name if not explicitly set
        Integer autoScriptIndex = extractScriptIndex();
        if (autoScriptIndex != null) {
            setScriptIndex(autoScriptIndex);
        }
    }

    public Block(BpmnPath path, String name, BlockType type, NodeType nodeType, String id) {
        this.id = id;
        this.path = path;
        this.attributes = new HashMap<>();
        setAttribute("name", name);
        setAttribute("type", type != null ? type.toString() : null);
        setAttribute("nodeType", nodeType != null ? nodeType.toString() : null);
        // Auto-populate scriptIndex from name if not explicitly set
        Integer autoScriptIndex = extractScriptIndex();
        if (autoScriptIndex != null) {
            setScriptIndex(autoScriptIndex);
        }
    }

    public Block(BpmnPath path, String name, String content, NodeType nodeType) {
        this(path, name, BlockType.FUNCTION, nodeType);
        setAttribute("content", content);
        // Auto-populate scriptIndex is already done in the parent constructor
    }

    public Block(BpmnPath path, String name, String content, NodeType nodeType, String id) {
        this(path, name, BlockType.FUNCTION, nodeType, id);
        setAttribute("content", content);
        // Auto-populate scriptIndex is already done in the parent constructor
    }

    // Direct field getters
    public String getId() {
        return id;
    }

    public BpmnPath getPath() {
        return path;
    }

    // Generic attribute methods
    public String getAttribute(String key) {
        return attributes.get(key);
    }

    public void setAttribute(String key, String value) {
        if (value != null) {
            attributes.put(key, value);
        }
    }

    public Map<String, String> getAttributes() {
        return new HashMap<>(attributes);
    }

    // Backward compatibility getters from attributes
    public String getName() {
        return getAttribute("name");
    }

    public BlockType getType() {
        String typeStr = getAttribute("type");
        return typeStr != null ? BlockType.valueOf(typeStr) : null;
    }

    public NodeType getNodeType() {
        String nodeTypeStr = getAttribute("nodeType");
        return nodeTypeStr != null ? NodeType.valueOf(nodeTypeStr) : null;
    }

    public String getContent() {
        return getAttribute("content");
    }

    public void setContent(String content) {
        setAttribute("content", content);
    }

    public Integer getScriptIndex() {
        String scriptIndex = getAttribute("scriptIndex");
        return scriptIndex != null ? Integer.valueOf(scriptIndex) : extractScriptIndex();
    }

    public void setScriptIndex(Integer scriptIndex) {
        setAttribute("scriptIndex", scriptIndex != null ? scriptIndex.toString() : null);
    }

    private Integer extractScriptIndex() {
        String name = getName();
        if (name != null) {
            String[] splitName = name.split("_");
            int length = splitName.length;
            try {
                return Integer.valueOf(splitName[length - 1]);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}
