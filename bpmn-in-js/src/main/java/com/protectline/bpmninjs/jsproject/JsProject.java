package com.protectline.bpmninjs.jsproject;

import com.protectline.bpmninjs.common.block.Block;

import java.io.IOException;
import java.util.List;

public interface JsProject {
    
    /**
     * Returns the blocks parsed from the JS project
     */
    List<Block> getBlocks() throws IOException;
    
    /**
     * Returns the JS content string from the project
     */
    String getJsContent() throws IOException;
    
    /**
     * Writes the updated JS content back to the project
     */
    void writeJsContent(String updatedContent) throws IOException;
}