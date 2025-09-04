package com.protectline.bpmninjs.xmlparser;

import com.protectline.bpmninjs.xmlparser.parser.Element;
import com.protectline.bpmninjs.xmlparser.util.BpmnTokenDefinition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class BpmnEndToEndTest {
    
    @TempDir
    Path tempDir;
    
    @Test
    void should_preserve_simple_bpmn_attributes_after_roundtrip() throws IOException {
        // Given
        String originalContent = readBpmnFile("src/test/resources/simple.bpmn");
        Path backupFile = tempDir.resolve("simple_backup.bpmn");
        Files.writeString(backupFile, originalContent);
        
        XmlParser parser = new XmlParser();
        BpmnTokenDefinition tokenDefinition = new BpmnTokenDefinition();

        // When - Parse, delete original, write regenerated
        Element rootElement = parser.getRootElement(originalContent, tokenDefinition);
        String regeneratedXml = rootElement.toXml();
        Path regeneratedFile = tempDir.resolve("simple_regenerated.bpmn");
        Files.writeString(regeneratedFile, regeneratedXml);
        
        // Then - Compare regenerated with backup
        String backupContent = Files.readString(backupFile);
        assertXmlEquals(backupContent, regeneratedXml, 
                "Les attributs du BPMN simple devraient être préservés lors du roundtrip");
    }

    @Test
    void should_preserve_urls_in_bpmn_namespace_attributes() throws IOException {
        // Given - Real BPMN file with URLs in namespace attributes
        String originalContent = readBpmnFile("src/test/resources/CreateCustomer_Dev.bpmn");
        Path backupFile = tempDir.resolve("CreateCustomer_Dev_backup.bpmn");
        Files.writeString(backupFile, originalContent);
        
        XmlParser parser = new XmlParser();
        BpmnTokenDefinition tokenDefinition = new BpmnTokenDefinition();

        // When - Parse, delete original, write regenerated
        Element rootElement = parser.getRootElement(originalContent, tokenDefinition);
        String regeneratedXml = rootElement.toXml();
        Path regeneratedFile = tempDir.resolve("CreateCustomer_Dev_regenerated.bpmn");
        Files.writeString(regeneratedFile, regeneratedXml);
        
        // Then - URLs in attributes should be preserved exactly
        String backupContent = Files.readString(backupFile);
        
        // Check specific URL attributes that should be preserved
        assertThat(regeneratedXml).contains("xmlns:bpmn=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"")
                .withFailMessage("L'URL du namespace BPMN devrait être préservée complètement");
        assertThat(regeneratedXml).contains("xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\"")
                .withFailMessage("L'URL du namespace BPMNDI devrait être préservée complètement");
        
        // The files should be semantically equivalent
        assertXmlEquals(backupContent, regeneratedXml, 
                "Les URLs dans les attributs de namespace devraient être préservées lors du roundtrip");
    }

    private static String readBpmnFile(String first) throws IOException {
        Path originalBpmnPath = Path.of(first);
        String originalContent = Files.readString(originalBpmnPath);
        return originalContent;
    }
    
    /**
     * Compare deux strings XML de manière sémantique en utilisant XMLUnit
     * comme dans le module bpmn-in-js (inspiré de compareBpmnFiles)
     */
    private static void assertXmlEquals(String expected, String actual, String message) {
        org.xmlunit.diff.Diff xmlDiff = org.xmlunit.builder.DiffBuilder.compare(expected)
                .withTest(actual)
                .normalizeWhitespace()
                .ignoreComments()
                .ignoreWhitespace()
                .build();

        java.util.Iterator<org.xmlunit.diff.Difference> iter = xmlDiff.getDifferences().iterator();
        java.util.List<String> differences = new java.util.ArrayList<>();
        while (iter.hasNext()) {
            differences.add(iter.next().toString());
        }

        if (!differences.isEmpty()) {
            System.out.println("=== XML DIFFERENCES ===");
            differences.forEach(System.out::println);
            fail(message + ". Differences found: " + differences.size());
        }
    }
}
