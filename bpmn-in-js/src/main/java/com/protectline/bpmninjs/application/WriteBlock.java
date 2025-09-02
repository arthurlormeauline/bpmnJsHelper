package com.protectline.bpmninjs.application;

import com.protectline.bpmninjs.common.block.Block;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface WriteBlock {
    void writeBlocksToFile(List<Block> blocks, Path blocksFile) throws IOException;

    List<Block> readBlocksFromFile(Path blocksfile) throws IOException;
}
