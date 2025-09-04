package com.protectline.bpmninjs.application.entrypoint.tobpmn;

import com.protectline.bpmninjs.model.common.block.Block;
import com.protectline.bpmninjs.engine.tobpmn.spi.BlockFromElement;
import com.protectline.bpmninjs.engine.tobpmn.jstoblocks.BlockFromElementResult;
import com.protectline.bpmninjs.application.template.Template;

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
