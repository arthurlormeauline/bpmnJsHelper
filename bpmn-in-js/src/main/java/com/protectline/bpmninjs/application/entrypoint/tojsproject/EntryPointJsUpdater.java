package com.protectline.bpmninjs.application.entrypoint.tojsproject;

import com.protectline.bpmninjs.model.common.block.Block;
import com.protectline.bpmninjs.engine.tojsproject.spi.JsUpdater;
import com.protectline.bpmninjs.application.template.Template;

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
