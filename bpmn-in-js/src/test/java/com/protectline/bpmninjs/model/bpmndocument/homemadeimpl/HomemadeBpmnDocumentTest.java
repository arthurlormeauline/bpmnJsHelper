package com.protectline.bpmninjs.model.bpmndocument.homemadeimpl;

import com.protectline.bpmninjs.model.bpmndocument.api.model.Node;
import com.protectline.bpmninjs.model.bpmndocument.api.model.NodeType;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HomemadeBpmnDocumentTest {

    @Test
    void should_parse_simple_bpmn_document() throws IOException {
        // Given - Créer un fichier BPMN simple pour le test
        String simpleBpmn = """
            <?xml version="1.0" encoding="UTF-8"?>
            <bpmn:definitions xmlns:bpmn="http://www.omg.org/spec/BPMN/20100524/MODEL">
              <bpmn:process id="TestProcess">
                <bpmn:startEvent id="start1" name="Start">
                  <bpmn:outgoing>flow1</bpmn:outgoing>
                </bpmn:startEvent>
                <bpmn:scriptTask id="script1" name="Script Task">
                  <bpmn:script>console.log('test script');</bpmn:script>
                </bpmn:scriptTask>
                <bpmn:serviceTask id="service1" name="Service Task">
                  <bpmn:script>var result = processData();</bpmn:script>
                </bpmn:serviceTask>
              </bpmn:process>
            </bpmn:definitions>
            """;

        Path tempFile = Files.createTempFile("test", ".bpmn");
        Files.writeString(tempFile, simpleBpmn);
        
        try {
            // When
            HomemadeBpmnDocument document = new HomemadeBpmnDocument(tempFile.toFile());
            List<Node> nodes = document.getFirstLevelElements();

            // Then
            assertThat(nodes).hasSize(3); // 1 start, 1 script, 1 service

            // Vérifier les types de noeuds
            List<NodeType> nodeTypes = nodes.stream()
                    .map(Node::getType)
                    .toList();
            
            assertThat(nodeTypes).contains(NodeType.START, NodeType.SCRIPT, NodeType.SERVICE_TASK);

            // Vérifier qu'on peut récupérer les attributs
            Node startNode = nodes.stream()
                    .filter(node -> node.getType() == NodeType.START)
                    .findFirst()
                    .orElseThrow();
            
            assertThat(startNode.getAttributeValue("id")).isEqualTo("start1");
            assertThat(startNode.getAttributeValue("name")).isEqualTo("Start");

            // Vérifier qu'on peut récupérer les scripts
            Node scriptNode = nodes.stream()
                    .filter(node -> node.getType() == NodeType.SCRIPT)
                    .findFirst()
                    .orElseThrow();
            
            List<String> scripts = scriptNode.getScripts();
            assertThat(scripts).hasSize(1);
            assertThat(scripts.get(0)).contains("console.log('test script');");

        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    void should_handle_node_names_correctly() throws IOException {
        // Given
        String bpmnWithNamespaces = """
            <?xml version="1.0" encoding="UTF-8"?>
            <bpmn:definitions xmlns:bpmn="http://www.omg.org/spec/BPMN/20100524/MODEL">
              <bpmn:process id="TestProcess">
                <bpmn:startEvent id="start1"/>
                <bpmn:scriptTask id="script1"/>
                <bpmn:serviceTask id="service1"/>
              </bpmn:process>
            </bpmn:definitions>
            """;

        Path tempFile = Files.createTempFile("test", ".bpmn");
        Files.writeString(tempFile, bpmnWithNamespaces);
        
        try {
            // When
            HomemadeBpmnDocument document = new HomemadeBpmnDocument(tempFile.toFile());
            List<Node> nodes = document.getFirstLevelElements();

            // Then
            for (Node node : nodes) {
                String nodeName = node.getNodeName();
                // Vérifier que les noms de noeud n'ont pas de préfixe namespace
                assertThat(nodeName).doesNotContain("bpmn:");
                assertThat(nodeName).isIn("startEvent", "scriptTask", "serviceTask");
            }

        } finally {
            Files.deleteIfExists(tempFile);
        }
    }
}