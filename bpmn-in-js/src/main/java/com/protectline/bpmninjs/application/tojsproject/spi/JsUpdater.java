package com.protectline.bpmninjs.application.tojsproject.spi;

import com.protectline.bpmninjs.common.block.Block;

import java.util.List;

public interface JsUpdater {
    String update(String intput, List<Block> blocks);
}
