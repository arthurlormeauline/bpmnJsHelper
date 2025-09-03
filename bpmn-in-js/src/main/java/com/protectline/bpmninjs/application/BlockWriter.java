package com.protectline.bpmninjs.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.common.block.jsonblock.FunctionBlocksList;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class BlockWriter implements WriteBlock {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void writeBlocksToFile(List<Block> blocks, Path filePath) throws IOException {
        FunctionBlocksList blocksList = new FunctionBlocksList(blocks, 0);
        objectMapper.writeValue(filePath.toFile(), blocksList);
    }

    @Override
    public List<Block> readBlocksFromFile(Path blocksfile) throws IOException {
        FunctionBlocksList blocksList = objectMapper.readValue(blocksfile.toFile(), FunctionBlocksList.class);
        return blocksList.toBlocks();
    }
}
