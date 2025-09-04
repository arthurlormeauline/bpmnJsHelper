package com.protectline.bpmninjs.xmlparser;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TokenParserTreeTraversalTest {

    @Test
    public void testTreeTraversalPutsRootFirst() {
        // Given - Créer manuellement un arbre : root -> parent -> child
        Element child = new Element("child", Map.of("id", "c1"), "child content");
        Element parent = new Element("parent", Map.of("id", "p1"), List.of(child));
        Element root = new Element("root", Map.of("id", "r1"), List.of(parent));

        // When - Traverser l'arbre
        List<Element> traversedElements = new ArrayList<>();
        collectAllElements(root, traversedElements);

        // Then - Vérifier que le root est en premier
        assertEquals(3, traversedElements.size());
        assertEquals("root", traversedElements.get(0).getElementName());
        assertEquals("parent", traversedElements.get(1).getElementName());  
        assertEquals("child", traversedElements.get(2).getElementName());
    }

    @Test
    public void testExtractAllElementsExceptRootRemovesFirst() {
        // Given - Créer un arbre avec plusieurs niveaux
        Element grandChild = new Element("grandchild", Map.of(), "gc content");
        Element child1 = new Element("child1", Map.of(), List.of(grandChild));
        Element child2 = new Element("child2", Map.of(), "child2 content");
        Element root = new Element("root", Map.of(), List.of(child1, child2));

        // When - Extraire tous les éléments sauf le root
        List<Element> result = extractAllElementsExceptRoot(root);

        // Then - Le root ne doit pas être dans la liste
        assertEquals(3, result.size()); // child1, grandchild, child2 (pas root)
        
        // Vérifier qu'aucun élément n'est le root
        for (Element element : result) {
            assertNotEquals("root", element.getElementName());
        }
        
        // Vérifier que tous les autres éléments sont présents
        assertTrue(result.stream().anyMatch(e -> "child1".equals(e.getElementName())));
        assertTrue(result.stream().anyMatch(e -> "child2".equals(e.getElementName())));
        assertTrue(result.stream().anyMatch(e -> "grandchild".equals(e.getElementName())));
    }

    @Test 
    public void testExtractFromEmptyRootReturnsEmpty() {
        // Given - Root fictif vide (nom vide, pas d'enfants)
        Element emptyRoot = new Element("", Map.of(), new ArrayList<>());

        // When
        List<Element> result = extractAllElementsExceptRoot(emptyRoot);

        // Then - Doit être vide car pas d'enfants et le root vide est supprimé
        assertEquals(0, result.size());
    }

    @Test
    public void testExtractFromFictionalRootWithChildren() {
        // Given - Root fictif (nom vide) avec des enfants
        Element child1 = new Element("element1", Map.of(), "content1");
        Element child2 = new Element("element2", Map.of(), "content2");
        Element fictionalRoot = new Element("", Map.of(), List.of(child1, child2));

        // When
        List<Element> result = extractAllElementsExceptRoot(fictionalRoot);

        // Then - Doit contenir tous les enfants mais pas le root fictif
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(e -> "element1".equals(e.getElementName())));
        assertTrue(result.stream().anyMatch(e -> "element2".equals(e.getElementName())));
        assertTrue(result.stream().noneMatch(e -> "".equals(e.getElementName())));
    }

    // Helper method to test - copy of the private method from TokenParser
    private void collectAllElements(Element element, List<Element> result) {
        result.add(element);
        for (Element child : element.getChildren()) {
            collectAllElements(child, result);
        }
    }

    // Method under test - copy of the private method from TokenParser
    private List<Element> extractAllElementsExceptRoot(Element rootElement) {
        List<Element> allElements = new ArrayList<>();
        collectAllElements(rootElement, allElements);
        
        // Le root est toujours en premier dans la traversée, donc on l'enlève
        if (!allElements.isEmpty()) {
            allElements.remove(0);
        }
        
        return allElements;
    }
}