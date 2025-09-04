package com.protectline.bpmninjs.xmlparser;

import com.protectline.bpmninjs.xmlparser.tokendefinition.BpmnTokenDefinition;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BpmnDebugTest {
    
    @Test
    public void debugSimpleBpmnDifferences() throws IOException {
        // Lire le fichier original
        String originalContent = Files.readString(Path.of("src/test/resources/simple.bpmn"));
        
        // Parser et régénérer
        XmlParser parser = new XmlParser();
        BpmnTokenDefinition tokenDefinition = new BpmnTokenDefinition();
        Element rootElement = parser.getRootElement(originalContent, tokenDefinition);
        String regeneratedXml = rootElement.toXml();
        
        // Afficher les longueurs
        System.out.println("Original length: " + originalContent.length());
        System.out.println("Regenerated length: " + regeneratedXml.length());
        
        // Afficher caractère par caractère les différences
        System.out.println("\n=== ORIGINAL ===");
        printWithCharCodes(originalContent);
        
        System.out.println("\n=== REGENERATED ===");
        printWithCharCodes(regeneratedXml);
        
        // Comparer caractère par caractère
        System.out.println("\n=== DIFFERENCES ===");
        int minLength = Math.min(originalContent.length(), regeneratedXml.length());
        for (int i = 0; i < minLength; i++) {
            char origChar = originalContent.charAt(i);
            char regenChar = regeneratedXml.charAt(i);
            if (origChar != regenChar) {
                System.out.printf("Position %d: orig='%c'(%d) vs regen='%c'(%d)\n", 
                        i, origChar, (int)origChar, regenChar, (int)regenChar);
            }
        }
        
        if (originalContent.length() != regeneratedXml.length()) {
            System.out.println("Length difference: " + (regeneratedXml.length() - originalContent.length()));
        }
    }
    
    private void printWithCharCodes(String content) {
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (c == '\n') {
                System.out.print("\\n");
            } else if (c == '\r') {
                System.out.print("\\r");
            } else if (c == ' ') {
                System.out.print("·"); // Visible space character
            } else {
                System.out.print(c);
            }
        }
        System.out.println();
    }
}