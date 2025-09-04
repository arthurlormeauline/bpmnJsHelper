package com.protectline.bpmninjs.engine.tobpmn.spi;

import com.protectline.bpmninjs.model.common.block.Block;

import java.util.List;

public interface UpdateBlock {
    void updateBlockContent(List<Block> blocksFromFile, Block blockFromJs);
}
