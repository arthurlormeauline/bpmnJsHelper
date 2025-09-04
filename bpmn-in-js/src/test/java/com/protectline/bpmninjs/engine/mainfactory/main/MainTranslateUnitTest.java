package com.protectline.bpmninjs.engine.mainfactory.main;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MainTranslateUnitTest {

    @Test
    void should_create_factory_without_working_directory() {
        // Given & When
        MainTranslateUnit factory = new MainTranslateUnit();
        
        // Then
        assertNotNull(factory);
        assertEquals(1, factory.getElementNames().size());
        assertTrue(factory.getElementNames().contains("main"));
        assertTrue(factory.createBlockBuilder().isPresent());
        assertTrue(factory.createJsUpdater().isPresent());
        assertTrue(factory.createBlockFromElement().isPresent());
    }
}
