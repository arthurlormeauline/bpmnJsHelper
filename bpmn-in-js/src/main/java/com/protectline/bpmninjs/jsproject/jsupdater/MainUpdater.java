package com.protectline.bpmninjs.jsproject.jsupdater;

import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.jsproject.JsUpdater;
import com.protectline.bpmninjs.jsproject.updatertemplate.JsUpdaterTemplate;

import java.util.List;

public class MainUpdater implements JsUpdater {


    private final JsUpdaterTemplate template;

    public MainUpdater(JsUpdaterTemplate jsUpdaterTemplate) {
        this.template = jsUpdaterTemplate;
    }

    @Override
    public String update(String input, List<Block> blocks) {
        return input.replaceAll(template.getFlag(), template.getTemplate());
    }
}
