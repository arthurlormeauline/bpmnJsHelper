package com.protectline.bpmninjs.translateunitfactory.entrypoint;

import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.jsproject.JsUpdater;
import com.protectline.bpmninjs.translateunitfactory.template.Template;

import java.util.List;

public class EntryPointJsUpdater implements JsUpdater {


    private final Template template;

    public EntryPointJsUpdater(Template jsUpdaterTemplate) {
        this.template = jsUpdaterTemplate;
    }

    @Override
    public String update(String input, List<Block> blocks) {
        return input.replaceAll(template.getFlag(), template.getTemplate());
    }
}
