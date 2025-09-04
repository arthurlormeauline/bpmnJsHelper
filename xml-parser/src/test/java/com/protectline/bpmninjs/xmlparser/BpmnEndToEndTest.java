package com.protectline.bpmninjs.xmlparser;

import com.protectline.bpmninjs.xmlparser.parser.Element;
import com.protectline.bpmninjs.xmlparser.util.BpmnTokenDefinition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BpmnEndToEndTest {
    
    @TempDir
    Path tempDir;
    
    @Test
    public void testSimpleBpmnParseWriteParseCycle() throws IOException {
        // Given
        String originalContent = readBpmnFile("src/test/resources/simple.bpmn");

        // When
        XmlParser parser = new XmlParser();
        BpmnTokenDefinition tokenDefinition = new BpmnTokenDefinition();
        Element rootElement = parser.getRootElement(originalContent, tokenDefinition);
        
        String regeneratedXml = rootElement.toXml();
        Path generate = tempDir.resolve("simple_regenerated.bpmn");
        Files.writeString(generate, regeneratedXml);

        // Then
        Element reparsedRoot = parser.getRootElement(regeneratedXml, tokenDefinition);
        String reRegeneratedXml = reparsedRoot.toXml();
        
        // Vérifier que le BPMN original est sémantiquement identique au BPMN régénéré
        // (utiliser XMLUnit comme dans le module bpmn-in-js pour une comparaison sémantique)
        assertXmlEquals(originalContent, regeneratedXml,
                "Le BPMN original devrait être sémantiquement identique au BPMN régénéré après parsing puis réécriture");
        
        // Vérifier que le XML régénéré est stable lors de multiples cycles
        assertEquals(regeneratedXml, reRegeneratedXml, 
                "Le XML régénéré devrait être stable lors de multiples cycles de génération");

    }

    @Test
    public void testRealBpmnWithXmlDeclarationParseWriteParseCycle() throws IOException {
        // Given
        String originalContent = readBpmnFile("src/test/resources/CreateCustomer_Dev.bpmn");

        // back up file
        Files.writeString(tempDir.resolve("CreateCustomer_Dev_backup.bpmn"), originalContent);

        // When
        XmlParser parser = new XmlParser();
        BpmnTokenDefinition tokenDefinition = new BpmnTokenDefinition();
        Element rootElement = parser.getRootElement(originalContent, tokenDefinition);
        
        // Réécrire le fichier parsé
        String regeneratedXml = rootElement.toXml();
        Files.writeString(tempDir.resolve("CreateCustomer_Dev_regenerated.bpmn"), regeneratedXml);

        // Parser à nouveau le fichier régénéré
        Element reparsedRoot = parser.getRootElement(regeneratedXml, tokenDefinition);
        
        // Comparer les deux structures (original parsé vs régénéré parsé)
        // On compare les représentations string des structures
        assertEquals(rootElement.toString(), reparsedRoot.toString(), 
                "Le fichier parsé puis régénéré devrait être identique à l'original parsé");
        
        // Vérifier aussi que le XML régénéré peut être reparsé sans erreur
        String reRegeneratedXml = reparsedRoot.toXml();
        assertEquals(regeneratedXml, reRegeneratedXml, 
                "Le XML régénéré devrait être stable lors de multiples cycles de génération");
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
            org.junit.jupiter.api.Assertions.fail(message + ". Differences found: " + differences.size());
        }
    }
}
