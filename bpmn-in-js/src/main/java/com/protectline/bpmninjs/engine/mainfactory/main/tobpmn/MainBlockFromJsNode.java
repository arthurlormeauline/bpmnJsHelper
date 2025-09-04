package com.protectline.bpmninjs.engine.mainfactory.main.tobpmn;

import com.protectline.bpmninjs.model.block.Block;
import com.protectline.bpmninjs.engine.tobpmn.spi.BlockFromJsNode;
import com.protectline.bpmninjs.engine.tobpmn.jstoblocks.BlockFromJsElementResult;
import com.protectline.bpmninjs.model.template.Template;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainBlockFromJsNode implements BlockFromJsNode {
    
    private final Template template;

    public MainBlockFromJsNode(Template template) {
        this.template = template;
    }
    
    @Override
    public BlockFromJsElementResult parse(String content, Map<String, String> attributes) {
        List<Block> blocks = new ArrayList<>();
        return new BlockFromJsElementResult(blocks, content);
    }
}
