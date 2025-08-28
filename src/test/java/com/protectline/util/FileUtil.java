package com.protectline.util;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;
import org.xmlunit.diff.DifferenceEvaluator;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
        String modifiedContent = Files.readString(bpmnFile);

        // back up expected file to restore at the end of comparison
        var backUpExpected = Files.readString(expectedModify);

        // change expected by a read write without changing it --> this add some attributes to XML without changing the process
        var modelInstance = Bpmn.readModelFromFile(expectedModify.toFile());
        Bpmn.writeModelToFile(expectedModify.toFile(), modelInstance);

        String expectedContent = Files.readString(expectedModify);

        Diff myDiff = DiffBuilder.compare(expectedContent)
                .withTest(modifiedContent)
                .normalizeWhitespace()
                .ignoreComments()
                .ignoreWhitespace()
                .build();


        Iterator<Difference> iter = myDiff.getDifferences().iterator();
        int size = 0;
        while (iter.hasNext()) {
            String diff = iter.next().toString();
            System.out.println(diff);
            size++;
        }

        assertThat(size).isEqualTo(0);

        // restore expected for other tests
        Files.writeString(expectedModify, backUpExpected);
    }

    public static Path getResourcePath(Class<?> resourceClass, String resource) throws URISyntaxException {
        return Path.of(Objects.requireNonNull(
                resourceClass.getClassLoader().getResource(resource)).toURI());
    }


}
