package com.protectline.bpmninjs.common.block.jsonblock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.common.block.FunctionBlock;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class FunctionJsonBlockUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void writeBlocksToFile(List<FunctionBlock> functionBlocks, Path filePath) throws IOException {
        FunctionBlocksList blocksList = new FunctionBlocksList(functionBlocks,0);
        objectMapper.writeValue(filePath.toFile(), blocksList);
    }

    public static List<Block> readBlocksFromFile(Path blocksfile) throws IOException {
        FunctionBlocksList blocksList = objectMapper.readValue(blocksfile.toFile(), FunctionBlocksList.class);
        return blocksList.toFunctionBlocks();
    }
}
