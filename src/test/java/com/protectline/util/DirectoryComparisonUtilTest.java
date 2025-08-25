package com.protectline.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class DirectoryComparisonUtilTest {

    private Path mainPath;
    private Path mainCopyPath;

    @BeforeEach
    void setUp() throws Exception {
        mainPath = Path.of(Objects.requireNonNull(
            getClass().getClassLoader().getResource("comparisonUtilTest/main")).toURI());
        mainCopyPath = Path.of(Objects.requireNonNull(
            getClass().getClassLoader().getResource("comparisonUtilTest/main_copy")).toURI());
    }

    @Test
    void should_return_true_when_directories_have_identical_content() throws Exception {
        boolean result = DirectoryComparisonUtil.areDirectoriesEqual(mainPath, mainCopyPath);
        
        assertTrue(result, 
            "Directories with identical content should be considered equal: " 
            + mainPath + " vs " + mainCopyPath);
    }

    @Test
    void should_return_true_when_comparing_directory_with_itself() throws Exception {
        boolean result = DirectoryComparisonUtil.areDirectoriesEqual(mainPath, mainPath);
        
        assertTrue(result, 
            "Directory should be equal to itself: " + mainPath);
    }

    @Test
    void should_return_false_when_one_directory_does_not_exist() throws Exception {
        Path nonExistentPath = Path.of("/non/existent/path");
        
        boolean result = DirectoryComparisonUtil.areDirectoriesEqual(mainPath, nonExistentPath);
        
        assertFalse(result, 
            "Comparison should return false when one directory does not exist");
    }

    @Test
    void should_return_false_when_both_directories_do_not_exist() throws Exception {
        Path nonExistentPath1 = Path.of("/non/existent/path1");
        Path nonExistentPath2 = Path.of("/non/existent/path2");
        
        boolean result = DirectoryComparisonUtil.areDirectoriesEqual(nonExistentPath1, nonExistentPath2);
        
        assertFalse(result, 
            "Comparison should return false when both directories do not exist");
    }
}