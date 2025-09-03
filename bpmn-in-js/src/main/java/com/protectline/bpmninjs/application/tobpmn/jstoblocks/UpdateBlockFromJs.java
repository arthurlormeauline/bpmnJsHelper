package com.protectline.bpmninjs.application.tobpmn.jstoblocks;

import com.protectline.bpmninjs.common.block.Block;

import java.util.List;

public interface UpdateBlockFromJs {
    void updateBlockContent(List<Block> blocksFromFile, Block blockFromJs);
}
