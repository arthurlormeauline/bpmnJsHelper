package com.protectline.bpmninjs.application.tojsproject.bpmntoblocks;

import com.protectline.bpmninjs.common.block.BlockWriter;
import com.protectline.bpmninjs.files.FileUtil;
import com.protectline.bpmninjs.application.mainfactory.MainFactory;
import com.protectline.bpmninjs.util.MainFactoryTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

import static com.protectline.bpmninjs.common.block.jsonblock.FunctionJsonBlockUtil.readBlocksFromFile;
import static com.protectline.bpmninjs.util.FileUtil.getResourcePath;

class EmptyLinesDebugTest {

    private FromBpmnToBlocks fromBpmnToBlocks;
    private FileUtil fileUtil;

    @BeforeEach
    void setup() throws URISyntaxException, IOException {
        var testDirectory = "tojsproject";
        var resourcePath = getResourcePath(EmptyLinesDebugTest.class, testDirectory);
        fileUtil = new FileUtil(resourcePath);
        MainFactory mainFactory = MainFactoryTestUtil.createWithDefaults(fileUtil);
        BlockWriter blockWriter = new BlockWriter();
        fromBpmnToBlocks = new FromBpmnToBlocks(fileUtil, mainFactory.getBlockBuilders(), blockWriter);
    }

    @Test
    void debug_empty_lines_blocks_generation() throws IOException {
        // Given
        var process = "empty-lines-test";

        // When
        fromBpmnToBlocks.createBlocksFromBpmn(process);

        // Then - Lire le fichier généré et l'afficher pour debug
        var actualBlocksFile = fileUtil.getBlocksFile(process);
        var actualBlocks = readBlocksFromFile(actualBlocksFile);
        
        System.out.println("=== FICHIER GÉNÉRÉ ===");
        System.out.println("Path: " + actualBlocksFile);
        
        if (Files.exists(actualBlocksFile)) {
            String content = Files.readString(actualBlocksFile);
            System.out.println("JSON Content:");
            System.out.println(content);
        }
        
        System.out.println("\n=== BLOCS PARSÉS ===");
        actualBlocks.stream()
            .forEach(block -> {
                System.out.println("Block name: " + block.getName());
                System.out.println("Block content: '" + block.getContent() + "'");
                System.out.println("Content length: " + block.getContent().length());
                
                // Analyse des lignes
                String[] lines = block.getContent().split("\\n", -1);
                System.out.println("Lines count (with -1): " + lines.length);
                for (int i = 0; i < lines.length; i++) {
                    System.out.println("Line " + i + ": '" + lines[i] + "'");
                }
                
                long newlineCount = block.getContent().chars().filter(c -> c == '\n').count();
                System.out.println("Newline count: " + newlineCount);
                System.out.println("Has double newline: " + block.getContent().contains("\\n\\n"));
            });
    }
}
