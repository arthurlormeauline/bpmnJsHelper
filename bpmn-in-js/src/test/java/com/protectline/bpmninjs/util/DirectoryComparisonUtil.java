package com.protectline.bpmninjs.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class DirectoryComparisonUtil {

    public static boolean areDirectoriesEqual(Path dir1, Path dir2) throws IOException {
        if (!Files.exists(dir1) || !Files.exists(dir2)) {
            return false;
        }

        if (!Files.isDirectory(dir1) || !Files.isDirectory(dir2)) {
            return false;
        }

        String[] files1 = getDirectoryContents(dir1);
        String[] files2 = getDirectoryContents(dir2);

        if (files1.length != files2.length) {
            return false;
        }

        Arrays.sort(files1);
        Arrays.sort(files2);

        if (!Arrays.equals(files1, files2)) {
            return false;
        }

        for (String fileName : files1) {
            Path file1 = dir1.resolve(fileName);
            Path file2 = dir2.resolve(fileName);

            if (Files.isDirectory(file1)) {
                if (!Files.isDirectory(file2)) {
                    return false;
                }
                if (!areDirectoriesEqual(file1, file2)) {
                    return false;
                }
            } else {
                if (Files.isDirectory(file2)) {
                    return false;
                }
                if (!areFilesEqual(file1, file2)) {
                    System.out.println("file : "+ file1.getFileName()+" is different");
                    return false;
                }
            }
        }
        return true;
    }

    private static String[] getDirectoryContents(Path dir) throws IOException {
        return Files.list(dir)
                .map(path -> path.getFileName().toString())
                .toArray(String[]::new);
    }

    private static boolean areFilesEqual(Path file1, Path file2) throws IOException {
        if (Files.size(file1) != Files.size(file2)) {
            return false;
        }

        byte[] content1 = Files.readAllBytes(file1);
        byte[] content2 = Files.readAllBytes(file2);

        return Arrays.equals(content1, content2);
    }
}
