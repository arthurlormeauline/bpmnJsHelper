package com.protectline.application.tobpmn.jstoblocks;

import com.protectline.common.block.Block;

import java.util.List;

public interface BlockUpdaterFromJs {
    void updateBlockContent(List<Block> blocksFromFile, Block blockFromJs);
}