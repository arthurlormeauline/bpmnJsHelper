package com.protectline.bpmninjs.engine.tojsproject.spi;

import com.protectline.bpmninjs.model.block.Block;

import java.util.List;

public interface JsUpdater {
    String update(String intput, List<Block> blocks);
}
