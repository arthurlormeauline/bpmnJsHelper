package com.protectline.bpmninjs.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                    System.out.println("FILE 1 : "+Files.readString(file1));
                    System.out.println("FILE 2 : "+Files.readString(file2));
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
        // Comparaison ligne par ligne
        List<String> lines1 = Files.readAllLines(file1);
        List<String> lines2 = Files.readAllLines(file2);

        if (!lines1.equals(lines2)) {
            System.out.println("=== LINE-BY-LINE COMPARISON DEBUG ===");
            System.out.println("File1 lines: " + lines1.size());
            System.out.println("File2 lines: " + lines2.size());
            
            int maxLines = Math.max(lines1.size(), lines2.size());
            for (int i = 0; i < maxLines; i++) {
                String line1 = i < lines1.size() ? lines1.get(i) : "<MISSING>";
                String line2 = i < lines2.size() ? lines2.get(i) : "<MISSING>";
                
                if (!line1.equals(line2)) {
                    System.out.println("Line " + (i+1) + " DIFF:");
                    System.out.println("  File1: \"" + line1 + "\"");
                    System.out.println("  File2: \"" + line2 + "\"");
                }
            }
        }

        return lines1.equals(lines2);
    }
}
