package com.protectline.bpmninjs.jsproject;

import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.jsproject.jsupdater.JsUpdaterFactory;
import com.protectline.bpmninjs.jsproject.updatertemplate.JsUpdaterTemplate;

import java.util.List;

public class TemplateProvider {

    private final List<JsUpdaterTemplate> updaterTemplates;

    public TemplateProvider(List<JsUpdaterTemplate> updaterTemplates) {
        this.updaterTemplates = updaterTemplates;
    }

    List<JsUpdater> getJsUpdaters(List<Block> blocks) {
        return JsUpdaterFactory.getJsUpdaters(blocks, updaterTemplates);
    }
}
