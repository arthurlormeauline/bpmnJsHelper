package com.protectline.bpmninjs.util;

import com.protectline.bpmninjs.engine.files.FileService;
import com.protectline.bpmninjs.model.block.Block;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static com.protectline.bpmninjs.model.block.persist.BlockUtil.readBlocksFromFile;
import static com.protectline.bpmninjs.util.DirectoryComparisonUtil.areDirectoriesEqual;
import static com.protectline.bpmninjs.util.FileUtil.logBpmnRunnerDiff;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssertUtil {

    public static void assertJsProjectIsEqualToExpected(FileService fileService, String process) throws IOException {
        Path outputDir = fileService.getJsProjectDirectory(process);
        assertTrue(Files.exists(outputDir), "JS project directory should exist: " + outputDir);
        assertTrue(Files.list(outputDir).findAny().isPresent(), "JS project directory should not be empty: " + outputDir);

        Path expectedProject = fileService.getWorkingDirectory().resolve("expectedJsProject").resolve(process);

        // Log line-by-line diff for BpmnRunner.js
        Path actualBpmnRunner = outputDir.resolve("BpmnRunner.js");
        Path expectedBpmnRunner = expectedProject.resolve("BpmnRunner.js");
        logBpmnRunnerDiff(actualBpmnRunner, expectedBpmnRunner);

        assertThat(areDirectoriesEqual(outputDir, expectedProject)).isTrue();
    }

    public static void assertBlocksAreEqualToExpected(FileService fileService, String process) throws IOException {
        // Read actual blocks from the generated blocks file
        Path actualBlocksFile = fileService.getBlocksFile(process);
        assertTrue(Files.exists(actualBlocksFile), "Blocks file should exist: " + actualBlocksFile);
        
        List<Block> actualBlocks = readBlocksFromFile(actualBlocksFile);
        
        // Read expected blocks from the expected blocks file
        Path expectedBlocksFile = fileService.getWorkingDirectory().resolve("expectedblocks").resolve(process).resolve(process + ".json");
        assertTrue(Files.exists(expectedBlocksFile), "Expected blocks file should exist: " + expectedBlocksFile);
        
        List<Block> expectedBlocks = readBlocksFromFile(expectedBlocksFile);
        
        // Compare blocks
        assertBlocksEqualIgnoringId(actualBlocks, expectedBlocks);
    }

    /**
     * Compare two blocks excluding the UUID (useful for tests)
     */
    public static boolean blockEqualsIgnoringId(Block block1, Block block2) {
        if (block1 == block2) return true;
        if (block1 == null || block2 == null) return false;
        
        return Objects.equals(block1.getPath(), block2.getPath()) &&
               Objects.equals(block1.getName(), block2.getName()) &&
               contentEquals(block1.getContent(), block2.getContent()) &&
               Objects.equals(block1.getType(), block2.getType()) &&
               Objects.equals(block1.getNodeType(), block2.getNodeType()) &&
               Objects.equals(block1.getScriptIndex(), block2.getScriptIndex());
    }

    /**
     * Compare two block lists excluding the UUIDs
     */
    public static boolean blockListEqualsIgnoringId(List<Block> list1, List<Block> list2) {
        if (list1 == list2) return true;
        if (list1 == null || list2 == null) return false;
        if (list1.size() != list2.size()) return false;
        
        for (int i = 0; i < list1.size(); i++) {
            if (!blockEqualsIgnoringId(list1.get(i), list2.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Assert that two block lists are equal, ignoring UUIDs
     */
    public static void assertBlocksEqualIgnoringId(List<Block> actual, List<Block> expected) {
        assertThat(actual.size()).as("Block list sizes should be equal").isEqualTo(expected.size());
        if (!blockListEqualsIgnoringId(actual, expected)) {
            debugBlockListComparison(actual, expected);
            assertThat(true).as("Block lists should be equal ignoring IDs").isFalse();
        }
    }

    /**
     * Assert that two blocks are equal, ignoring UUIDs
     */
    public static void assertBlockEqualIgnoringId(Block actual, Block expected) {
        assertThat(blockEqualsIgnoringId(actual, expected)).as("Blocks should be equal ignoring ID").isTrue();
    }

    /**
     * Debug - compare and display differences between two block lists
     */
    public static void debugBlockListComparison(List<Block> list1, List<Block> list2) {
        System.out.println("=== DEBUG BLOCK LIST COMPARISON ===");
        System.out.println("List1 size: " + list1.size());
        System.out.println("List2 size: " + list2.size());
        
        int maxSize = Math.max(list1.size(), list2.size());
        for (int i = 0; i < maxSize; i++) {
            System.out.println("\n--- Block " + i + " ---");
            Block block1 = i < list1.size() ? list1.get(i) : null;
            Block block2 = i < list2.size() ? list2.get(i) : null;
            
            if (block1 == null) {
                System.out.println("List1[" + i + "]: NULL");
            } else {
                System.out.println("List1[" + i + "]: " + blockToString(block1));
            }
            
            if (block2 == null) {
                System.out.println("List2[" + i + "]: NULL");
            } else {
                System.out.println("List2[" + i + "]: " + blockToString(block2));
            }
            
            if (block1 != null && block2 != null) {
                System.out.println("Equal (ignoring ID): " + blockEqualsIgnoringId(block1, block2));
                if (!blockEqualsIgnoringId(block1, block2)) {
                    debugBlockComparison(block1, block2);
                }
            }
        }
        System.out.println("===================================");
    }

    /**
     * Debug - compare and display differences between two blocks
     */
    public static void debugBlockComparison(Block block1, Block block2) {
        System.out.println("  === BLOCK DETAIL COMPARISON ===");
        System.out.println("  Path equals: " + Objects.equals(block1.getPath(), block2.getPath()));
        System.out.println("    Block1 path: '" + block1.getPath() + "'");
        System.out.println("    Block2 path: '" + block2.getPath() + "'");
        
        System.out.println("  Name equals: " + Objects.equals(block1.getName(), block2.getName()));
        System.out.println("    Block1 name: '" + block1.getName() + "'");
        System.out.println("    Block2 name: '" + block2.getName() + "'");
        
        System.out.println("  Content equals: " + Objects.equals(block1.getContent(), block2.getContent()));
        System.out.println("    Block1 content length: " + (block1.getContent() != null ? block1.getContent().length() : "null"));
        System.out.println("    Block2 content length: " + (block2.getContent() != null ? block2.getContent().length() : "null"));
        if (!Objects.equals(block1.getContent(), block2.getContent())) {
            System.out.println("    Block1 content: '" + escapeContent(block1.getContent()) + "'");
            System.out.println("    Block2 content: '" + escapeContent(block2.getContent()) + "'");
        }
        
        System.out.println("  Type equals: " + Objects.equals(block1.getType(), block2.getType()));
        System.out.println("    Block1 type: " + block1.getType());
        System.out.println("    Block2 type: " + block2.getType());
        
        System.out.println("  NodeType equals: " + Objects.equals(block1.getNodeType(), block2.getNodeType()));
        System.out.println("    Block1 nodeType: " + block1.getNodeType());
        System.out.println("    Block2 nodeType: " + block2.getNodeType());
        
        System.out.println("  ScriptIndex equals: " + Objects.equals(block1.getScriptIndex(), block2.getScriptIndex()));
        System.out.println("    Block1 scriptIndex: " + block1.getScriptIndex());
        System.out.println("    Block2 scriptIndex: " + block2.getScriptIndex());
        System.out.println("  ==============================");
    }

    private static String blockToString(Block block) {
        return String.format("Block{name='%s', path='%s', type=%s, nodeType=%s, scriptIndex=%s}", 
                            block.getName(), 
                            block.getPath(), 
                            block.getType(), 
                            block.getNodeType(), 
                            block.getScriptIndex());
    }

    /**
     * Compare content strings, normalizing line endings and trimming whitespace
     */
    private static boolean contentEquals(String content1, String content2) {
        if (content1 == content2) return true;
        if (content1 == null || content2 == null) return false;
        
        String normalized1 = normalizeContent(content1);
        String normalized2 = normalizeContent(content2);
        
        return Objects.equals(normalized1, normalized2);
    }
    
    /**
     * Normalize content by converting all line endings to \n and trimming whitespace
     */
    private static String normalizeContent(String content) {
        if (content == null) return null;
        return content
                .replace("\r\n", "\n")  // Convert Windows CRLF to LF
                .replace("\r", "\n")    // Convert old Mac CR to LF
                .trim();                // Remove leading/trailing whitespace
    }

    private static String escapeContent(String content) {
        if (content == null) return "null";
        return content
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t")
                .replace(" ", "·"); // Use middle dot to show spaces
    }
}
