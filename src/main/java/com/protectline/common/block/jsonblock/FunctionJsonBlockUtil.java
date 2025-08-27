package com.protectline.common.block.jsonblock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.protectline.common.block.Block;
import com.protectline.common.block.FunctionBlock;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class FunctionJsonBlockUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void writeBlocksToFile(List<FunctionBlock> functionBlocks, Path filePath) throws IOException {
        FunctionBlocksList blocksList = new FunctionBlocksList(functionBlocks,0);
        objectMapper.writeValue(filePath.toFile(), blocksList);
    }

    public static List<Block> readBlocksFromFile(Path filePath) throws IOException {
        FunctionBlocksList blocksList = objectMapper.readValue(filePath.toFile(), FunctionBlocksList.class);
        return blocksList.toFunctionBlocks();
    }

//    public static String toJsonString(List<FunctionBlock> functionBlocks) throws IOException {
//        FunctionBlocksList blocksList = new FunctionBlocksList(functionBlocks, 0);
//        return objectMapper.writeValueAsString(blocksList);
//    }
//
//    public static List<FunctionBlock> fromJsonString(String jsonString) throws IOException {
//        FunctionBlocksList blocksList = objectMapper.readValue(jsonString, FunctionBlocksList.class);
//        return blocksList.toFunctionBlocks();
//    }
}
