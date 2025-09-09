package com.protectline.bpmninjs.util;

import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
        String modifiedContent = Files.readString(bpmnFile);

        // back up expected file to restore at the end of comparison
        var backUpExpected = Files.readString(expectedModify);
        String expectedContent = Files.readString(expectedModify);
        Diff myDiff = DiffBuilder.compare(expectedContent)
                .withTest(modifiedContent)
                .ignoreComments()
                .checkForSimilar()
                .build();


        Iterator<Difference> iter = myDiff.getDifferences().iterator();
        int size = 0;
        while (iter.hasNext()) {
            String diff = iter.next().toString();
            System.out.println(diff);
            size++;
        }

        System.out.println(bpmnFile);
        assertThat(size).isEqualTo(0);

        // restore expected for other tests
        Files.writeString(expectedModify, backUpExpected);
    }

    private static void printXml(String title, String xml) {
        System.out.println(title);
        System.out.println(xml);
    }

    public static Path getResourcePath(Class<?> resourceClass, String resource) throws URISyntaxException {
        return Path.of(Objects.requireNonNull(
                resourceClass.getClassLoader().getResource(resource)).toURI());
    }

    public static void logBpmnRunnerDiff(Path actualFile, Path expectedFile) throws IOException {
        if (!Files.exists(actualFile) || !Files.exists(expectedFile)) {
            System.out.println("=== DIFF: One or both BpmnRunner.js files don't exist ===");
            System.out.println("Actual exists: " + Files.exists(actualFile));
            System.out.println("Expected exists: " + Files.exists(expectedFile));
            return;
        }

        List<String> actualLines = Files.readAllLines(actualFile);
        List<String> expectedLines = Files.readAllLines(expectedFile);

        System.out.println("=== BpmnRunner.js DIFF ===");
        System.out.println("Actual lines: " + actualLines.size() + ", Expected lines: " + expectedLines.size());

        int maxLines = Math.max(actualLines.size(), expectedLines.size());
        
        for (int i = 0; i < maxLines; i++) {
            String actualLine = i < actualLines.size() ? actualLines.get(i) : "<MISSING>";
            String expectedLine = i < expectedLines.size() ? expectedLines.get(i) : "<MISSING>";

            if (!actualLine.equals(expectedLine)) {
                System.out.printf("Line %d DIFF:%n", i + 1);
                System.out.printf("  Expected: %s%n", expectedLine);
                System.out.printf("  Actual  : %s%n", actualLine);
            }
        }
        System.out.println("=== END DIFF ===");
    }

}
