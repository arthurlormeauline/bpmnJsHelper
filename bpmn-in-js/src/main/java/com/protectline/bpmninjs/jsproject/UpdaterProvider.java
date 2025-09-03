package com.protectline.bpmninjs.jsproject;

import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.application.mainfactory.MainFactory;

import java.util.List;

public class UpdaterProvider {

    private final MainFactory mainFactory;

    public UpdaterProvider(MainFactory mainFactory) {
        this.mainFactory = mainFactory;
    }

    List<JsUpdater> getJsUpdaters(List<Block> blocks) {
        return mainFactory.getJsUpdaters(blocks);
    }
}
