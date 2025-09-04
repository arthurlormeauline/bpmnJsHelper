package com.protectline.bpmninjs.engine.mainfactory.main.tojsproject;

import com.protectline.bpmninjs.model.block.Block;
import com.protectline.bpmninjs.engine.tojsproject.spi.JsUpdater;
import com.protectline.bpmninjs.model.template.Template;

import java.util.List;

public class MainJsUpdater implements JsUpdater {


    private final Template template;

    public MainJsUpdater(Template jsUpdaterTemplate) {
        this.template = jsUpdaterTemplate;
    }

    @Override
    public String update(String input, List<Block> blocks) {
        return input.replaceAll(template.getFlag(), template.getTemplate());
    }
}
