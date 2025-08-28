package com.protectline.common.block;

import com.protectline.bpmndocument.model.BpmnPath;
import com.protectline.bpmndocument.model.NodeType;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static com.protectline.common.block.BlockType.FUNCTION;

@EqualsAndHashCode
@Getter
public class FunctionBlock extends Block {
   private String content;
   private Integer scriptIndex;

    public FunctionBlock(BpmnPath path, String name, String content, NodeType nodeType) {
        super(path, name, FUNCTION, nodeType);
        this.content = content;
        this.scriptIndex = extractScriptIndex();
    }

    public FunctionBlock(BpmnPath path, String name, String content, NodeType nodeType, String id) {
        super(path, name, FUNCTION, nodeType);
        this.id = id;
        this.content = content;
        this.scriptIndex = extractScriptIndex();
    }

    private Integer extractScriptIndex() {
        String[] splitName = getName().split("_");
        int length = splitName.length;
        Integer last = Integer.valueOf(splitName[length-1]);
        return last;
    }

    public void setContent(String content) {
        this.content=content;
    }
}
