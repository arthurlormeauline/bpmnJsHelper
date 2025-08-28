package com.protectline.jsproject.jsupdater;

import com.protectline.common.block.Block;
import com.protectline.jsproject.JsUpdater;
import com.protectline.jsproject.updatertemplate.JsUpdaterTemplate;

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
