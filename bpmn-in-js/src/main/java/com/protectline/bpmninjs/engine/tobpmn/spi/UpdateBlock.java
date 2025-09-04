package com.protectline.bpmninjs.engine.tobpmn.spi;

import com.protectline.bpmninjs.model.block.Block;

import java.util.List;

public interface UpdateBlock {
    void update(List<Block> blocksFromFile, Block blockFromJs);
}
