package com.protectline.bpmninjs.translateunitfactory;

import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.jsproject.blocksfactory.BlockFromElement;
import com.protectline.bpmninjs.jsproject.blocksfactory.BlockFromElementResult;
import com.protectline.bpmninjs.translateunit.TemplateForParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntryPointBlockFromElement implements BlockFromElement {
    
    private final TemplateForParser template;

    public EntryPointBlockFromElement(TemplateForParser template) {
        this.template = template;
    }
    
    @Override
    public BlockFromElementResult parse(String content, Map<String, String> attributes) {
        List<Block> blocks = new ArrayList<>();
        return new BlockFromElementResult(blocks, content);
    }
}
