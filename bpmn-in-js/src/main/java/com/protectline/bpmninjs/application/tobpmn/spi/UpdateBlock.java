package com.protectline.bpmninjs.application.tobpmn.spi;

import com.protectline.bpmninjs.common.block.Block;

import java.util.List;

public interface UpdateBlock {
    void updateBlockContent(List<Block> blocksFromFile, Block blockFromJs);
}
