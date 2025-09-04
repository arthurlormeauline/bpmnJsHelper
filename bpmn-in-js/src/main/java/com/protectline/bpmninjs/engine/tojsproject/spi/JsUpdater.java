package com.protectline.bpmninjs.engine.tojsproject.spi;

import com.protectline.bpmninjs.model.common.block.Block;

import java.util.List;

public interface JsUpdater {
    String update(String intput, List<Block> blocks);
}
