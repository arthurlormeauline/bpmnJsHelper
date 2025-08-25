package com.protectline.bpmndocument;

import com.protectline.bpmndocument.model.element.Attribute;
import com.protectline.bpmndocument.model.element.Element;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;

import static com.protectline.bpmndocument.model.element.ElementType.SCRIPT;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class BpmnDocumentTest {
    BpmnDocument bpmnDocument;

    @BeforeEach
    void setup() throws URISyntaxException {
        bpmnDocument = new BpmnDocument(Path.of(BpmnDocumentTest.class.getClassLoader().getResource("tests").toURI()),
                "tus.prc.actionCombine");
    }

    @Test
    void should_extract_all_script() {
        // Given

        // When
        var elements = bpmnDocument.getAllScripts();

        // Then
        ////////////
        var startScript = new Element();
        var startEventAttributes = List.of(new Attribute("scriptFormat", "JavaScript"));
        startScript.setPath(new Pair<>("Event_1u1d1qi", List.of(0, 0, 0)));
        startScript.setType(SCRIPT);
        startScript.setAttributes(startEventAttributes);
        startScript.setContent("start event script");

        ////////////
        var getDeviceByNameUrlScript = new Element();
        var getDeviceByNameUrlScriptAttributes = List.of(
                new Attribute("scriptFormat", "JavaScript")
        );
        getDeviceByNameUrlScript.setPath(
                new Pair<>("Activity_0rwauzg", List.of(0, 0, 0, 0, 0)));
        getDeviceByNameUrlScript.setType(SCRIPT);
        getDeviceByNameUrlScript.setAttributes(getDeviceByNameUrlScriptAttributes);
        getDeviceByNameUrlScript.setContent("get device by name url scrip");

        ////////////
        var getDeviceByNameOutputScript = new Element();
        var getDeviceByNameOutputScripAttributes = List.of(
                new Attribute("scriptFormat", "JavaScript")
        );
        getDeviceByNameOutputScript.setPath(
                new Pair<>("Activity_0rwauzg", List.of(0, 0, 0, 3, 0)));
        getDeviceByNameOutputScript.setType(SCRIPT);
        getDeviceByNameOutputScript.setAttributes(getDeviceByNameOutputScripAttributes);
        getDeviceByNameOutputScript.setContent("get device by name url scrip");

        ////////////
        var delayDefinitionScript = new Element();
        var delayDefinitionScriptAttributes = List.of(
                new Attribute("scriptFormat", "JavaScript")
        );
        delayDefinitionScript.setPath(
                new Pair<>("Activity_0q6wfo8", List.of(2)));
        delayDefinitionScript.setType(SCRIPT);
        delayDefinitionScript.setAttributes(delayDefinitionScriptAttributes);
        delayDefinitionScript.setContent("delay definition scrip");

        assertThat(elements.containsAll(List.of(startScript,
                delayDefinitionScript,
                getDeviceByNameOutputScript,
                getDeviceByNameUrlScript))).isTrue();
    }

}
