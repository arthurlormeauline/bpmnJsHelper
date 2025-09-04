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
        
        // Check XML declaration preservation
        assertThat(normalizeString(getFirstLine(regeneratedXml))).isEqualTo(normalizeString(getFirstLine(backupContent)))
                .withFailMessage("La déclaration XML devrait être préservée identiquement");
        
        // Check that attributes are preserved (basic attribute order check)
        assertThat(regeneratedXml).contains("attr=\"value\"")
                .withFailMessage("Les attributs simples devraient être préservés");
        assertThat(regeneratedXml).contains("id=\"test\"")
                .withFailMessage("Les attributs des éléments auto-fermants devraient être préservés");
        
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
        
        // Check XML declaration preservation
        assertThat(normalizeString(getFirstLine(regeneratedXml))).isEqualTo(normalizeString(getFirstLine(backupContent)))
                .withFailMessage("La déclaration XML devrait être préservée identiquement");
        
        // Check specific URL attributes that should be preserved
        assertThat(regeneratedXml).contains("xmlns:bpmn=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"")
                .withFailMessage("L'URL du namespace BPMN devrait être préservée complètement");
        assertThat(regeneratedXml).contains("xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\"")
                .withFailMessage("L'URL du namespace BPMNDI devrait être préservée complètement");
                
        // Check attribute order preservation in regenerated XML
        assertAttributeOrder(regeneratedXml, "xmlns:bpmn", "xmlns:bpmndi", 
                "Les attributs de namespace devraient apparaître dans le même ordre que dans le XML original");
        
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
     * Extract the first line from a string
     */
    private static String getFirstLine(String content) {
        if (content == null || content.isEmpty()) {
            return "";
        }
        String[] lines = content.split("\n", 2);
        return lines[0];
    }
    
    /**
     * Normalize string by trimming and handling line ending differences
     */
    private static String normalizeString(String content) {
        if (content == null) {
            return "";
        }
        return content.trim().replaceAll("\\r\\n|\\r|\\n", "\n");
    }
    
    /**
     * Assert that attributes appear in the expected order in XML content
     */
    private static void assertAttributeOrder(String xmlContent, String firstAttribute, String secondAttribute, String message) {
        int firstIndex = xmlContent.indexOf(firstAttribute + "=");
        int secondIndex = xmlContent.indexOf(secondAttribute + "=");
        
        assertThat(firstIndex).withFailMessage("Attribute '" + firstAttribute + "' not found in XML").isNotEqualTo(-1);
        assertThat(secondIndex).withFailMessage("Attribute '" + secondAttribute + "' not found in XML").isNotEqualTo(-1);
        assertThat(firstIndex).withFailMessage(message + " - " + firstAttribute + " should appear before " + secondAttribute)
                .isLessThan(secondIndex);
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
