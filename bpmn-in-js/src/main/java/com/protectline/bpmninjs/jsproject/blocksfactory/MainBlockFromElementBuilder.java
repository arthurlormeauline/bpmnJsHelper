package com.protectline.bpmninjs.jsproject.blocksfactory;

import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.jsproject.updatertemplate.TemplateForParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class MainBlockFromElementBuilder implements BlockFromElementBuilder {
    
    private final TemplateForParser template;
    
    MainBlockFromElementBuilder(TemplateForParser template) {
        this.template = template;
    }
    
    @Override
    public BlockFromElementResult parse(String content, Map<String, String> attributes) {
        List<Block> blocks = new ArrayList<>();
        return new BlockFromElementResult(blocks, content);
    }
}
