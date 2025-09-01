package com.protectline.bpmninjs.jsproject.blocksfactory;

import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.jsproject.updatertemplate.TemplateForParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class MainParser implements JsParser {
    
    private final TemplateForParser template;
    
    MainParser(TemplateForParser template) {
        this.template = template;
    }
    
    @Override
    public ParseResult parse(String content, Map<String, String> attributes) {
        List<Block> blocks = new ArrayList<>();
        return new ParseResult(blocks, content);
    }
}
