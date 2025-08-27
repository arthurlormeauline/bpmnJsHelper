package com.protectline.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileUtil {

    public static void copyDirectory(Path source, Path target) throws IOException {
        try (var walk = Files.walk(source)) {
            walk.forEach(sourcePath -> {
                try {
                    Path targetPath = target.resolve(source.relativize(sourcePath));
                    if (Files.isDirectory(sourcePath)) {
                        Files.createDirectories(targetPath);
                    } else {
                        Files.createDirectories(targetPath.getParent());
                        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Failed to copy " + sourcePath + " to " + target, e);
                }
            });
        }
    }

    public static void compareBpmnFiles(Path bpmnFile, Path expectedModify) throws IOException {
        byte[] modifiedContent = Files.readAllBytes(bpmnFile);
        byte[] expectedContent = Files.readAllBytes(expectedModify);

        if (!java.util.Arrays.equals(modifiedContent, expectedContent)) {
            System.out.println("=== FICHIERS DIFFÉRENTS ===");
            System.out.println("Fichier modifié (" + bpmnFile + "):");
            System.out.println(new String(modifiedContent));
            System.out.println("\n=== FICHIER ATTENDU ===");
            System.out.println("Fichier attendu (" + expectedModify + "):");
            System.out.println(new String(expectedContent));
            System.out.println("\n=== DIFFÉRENCES ===");

            String[] modifiedLines = new String(modifiedContent).split("\n");
            String[] expectedLines = new String(expectedContent).split("\n");

            int maxLines = Math.max(modifiedLines.length, expectedLines.length);
            for (int i = 0; i < maxLines; i++) {
                String modLine = i < modifiedLines.length ? modifiedLines[i] : "<MISSING>";
                String expLine = i < expectedLines.length ? expectedLines[i] : "<MISSING>";

                if (!modLine.equals(expLine)) {
                    System.out.println("Ligne " + (i + 1) + ":");
                    System.out.println("  Modifié : " + modLine);
                    System.out.println("  Attendu : " + expLine);
                }
            }
        }

        assertTrue(java.util.Arrays.equals(modifiedContent, expectedContent));
    }

}
