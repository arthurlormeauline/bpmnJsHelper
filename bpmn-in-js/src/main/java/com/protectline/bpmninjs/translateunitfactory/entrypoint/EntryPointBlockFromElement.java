package com.protectline.bpmninjs.translateunitfactory.entrypoint;

import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.jsproject.blocksfromelement.BlockFromElement;
import com.protectline.bpmninjs.jsproject.blocksfromelement.BlockFromElementResult;
import com.protectline.bpmninjs.translateunitfactory.template.Template;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntryPointBlockFromElement implements BlockFromElement {
    
    private final Template template;

    public EntryPointBlockFromElement(Template template) {
        this.template = template;
    }
    
    @Override
    public BlockFromElementResult parse(String content, Map<String, String> attributes) {
        List<Block> blocks = new ArrayList<>();
        return new BlockFromElementResult(blocks, content);
    }
}
