package com.protectline.common.block.jsonblock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.protectline.common.block.FunctionBlock;

import java.io.IOException;
import java.nio.file.Path;

public class FunctionJsonBlockUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void writeBlocksToFile(FunctionBlock functionBlock, Path filePath) throws IOException {
        FunctionJsonBlock jsonBlock = new FunctionJsonBlock(functionBlock);
        objectMapper.writeValue(filePath.toFile(), jsonBlock);
    }

    public static FunctionBlock readBlocksFromFile(Path filePath) throws IOException {
        FunctionJsonBlock jsonBlock = objectMapper.readValue(filePath.toFile(), FunctionJsonBlock.class);
        return jsonBlock.toFunctionBlock();
    }

    public static String toJsonString(FunctionBlock functionBlock) throws IOException {
        FunctionJsonBlock jsonBlock = new FunctionJsonBlock(functionBlock);
        return objectMapper.writeValueAsString(jsonBlock);
    }

    public static FunctionBlock fromJsonString(String jsonString) throws IOException {
        FunctionJsonBlock jsonBlock = objectMapper.readValue(jsonString, FunctionJsonBlock.class);
        return jsonBlock.toFunctionBlock();
    }
}
