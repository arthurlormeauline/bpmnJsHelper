package com.protectline.bpmninjs.camundaimpl;

import com.protectline.bpmninjs.model.bpmndocument.camundaimpl.DomElementUtil;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.xml.instance.DomElement;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DomElementUtilTest {

    @Test
    void should_preserve_empty_lines_in_script_content() throws Exception {
        // Given - Créons un script avec des lignes vides
        String xmlWithEmptyLines = """
            <bpmn:process>
                <bpmn:scriptTask>
                    <bpmn:script>line1
            
            line2
            
            line3</bpmn:script>
                </bpmn:scriptTask>
            </bpmn:process>
            """;
        
        // Parse avec DOM standard pour voir le comportement
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xmlWithEmptyLines.getBytes()));
        
        Element scriptElement = (Element) doc.getElementsByTagName("bpmn:script").item(0);
        String textContent = scriptElement.getTextContent();
        
        System.out.println("=== CONTENU DU SCRIPT ===");
        System.out.println("Raw content: '" + textContent + "'");
        System.out.println("Lines count: " + textContent.split("\\n").length);
        
        // Vérifier si les lignes vides sont préservées
        assertThat(textContent).contains("line1");
        assertThat(textContent).contains("line2"); 
        assertThat(textContent).contains("line3");
        
        // Compter les vraies nouvelles lignes
        long newlineCount = textContent.chars().filter(c -> c == '\n').count();
        System.out.println("Newline count: " + newlineCount);
    }
    
    @Test
    void should_test_real_bpmn_parsing_with_empty_lines() throws Exception {
        // Given - Parse le fichier BPMN test avec Camunda
        File bpmnFile = new File("src/test/resources/test-empty-lines.bpmn");
        BpmnModelInstance modelInstance = Bpmn.readModelFromFile(bpmnFile);
        
        // When - Extraire les scripts comme le fait vraiment notre code
        DomElement activityElement = modelInstance.getModelElementById("Activity_test").getDomElement();
        List<String> scripts = DomElementUtil.getScripts(activityElement);
        
        // Then - Vérifier le contenu
        assertThat(scripts).hasSize(1);
        String script = scripts.get(0);
        
        System.out.println("=== SCRIPT EXTRAIT AVEC CAMUNDA ===");
        System.out.println("Raw content: '" + script + "'");
        System.out.println("Length: " + script.length());
        
        // Afficher chaque ligne individuellement pour debugger
        String[] lines = script.split("\\n", -1); // -1 pour préserver les lignes vides
        System.out.println("Lines count (with -1): " + lines.length);
        for (int i = 0; i < lines.length; i++) {
            System.out.println("Line " + i + ": '" + lines[i] + "'");
        }
        
        // Vérifications
        assertThat(script).contains("line1");
        assertThat(script).contains("line2");
        assertThat(script).contains("line3");
        assertThat(script).contains("line4");
        
        // Vérifier que les lignes vides sont préservées
        long newlineCount = script.chars().filter(c -> c == '\n').count();
        System.out.println("Newline count: " + newlineCount);
        assertThat(newlineCount).isGreaterThanOrEqualTo(5); // Au moins 5 \n pour nos lignes vides
    }
}
