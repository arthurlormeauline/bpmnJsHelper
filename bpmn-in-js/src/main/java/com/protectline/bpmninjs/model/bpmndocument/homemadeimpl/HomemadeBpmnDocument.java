package com.protectline.bpmninjs.model.bpmndocument.homemadeimpl;

import com.protectline.bpmninjs.model.bpmndocument.api.BpmnDocument;
import com.protectline.bpmninjs.model.bpmndocument.api.model.Node;
import com.protectline.bpmninjs.model.bpmndocument.api.model.ScriptNode;
import com.protectline.bpmninjs.model.bpmndocument.api.model.ServiceTaskNode;
import com.protectline.bpmninjs.model.bpmndocument.api.model.StartNode;
import com.protectline.bpmninjs.xmlparser.XmlParser;
import com.protectline.bpmninjs.xmlparser.parser.Element;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class HomemadeBpmnDocument implements BpmnDocument {
    private Element rootElement;
    private File originalFile;

    public HomemadeBpmnDocument(File processFile) throws IOException {
        this.originalFile = processFile;

        String bpmnContent = Files.readString(processFile.toPath());
        XmlParser xmlParser = new XmlParser();
        this.rootElement = xmlParser.getRootElement(bpmnContent, new BpmnTokenDefinition());
    }

    @Override
    public List<Node> getFirstLevelElements() {
        List<Node> nodes = new ArrayList<>();

        nodes.addAll(getScriptNodes());
        nodes.addAll(getServiceTaskNodes());
        nodes.addAll(getStartNodes());

        return nodes;
    }

    private List<? extends StartNode> getStartNodes() {
        return HomemadeElementUtil.getElementsByElementName(rootElement, "bpmn:startEvent").stream()
                .map(HomemadeStartNode::new)
                .toList();
    }

    private List<? extends ServiceTaskNode> getServiceTaskNodes() {
        return HomemadeElementUtil.getElementsByElementName(rootElement, "bpmn:serviceTask").stream()
                .map(HomemadeServiceTaskNode::new)
                .toList();
    }

    private List<? extends ScriptNode> getScriptNodes() {
        return HomemadeElementUtil.getElementsByElementName(rootElement, "bpmn:scriptTask").stream()
                .map(HomemadeScriptNode::new)
                .toList();
    }

    @Override
    public void setScript(String elementId, Integer scriptIndex, String content) {
        // Pour l'instant, nous reconstruisons complètement l'arbre après modification
        // Une approche plus efficace nécessiterait des éléments mutables
        this.rootElement = updateScriptInElement(rootElement, elementId, scriptIndex, content);
    }

    private Element updateScriptInElement(Element element, String targetElementId, Integer scriptIndex, String newContent) {
        // Si c'est l'élément cible, modifier ses scripts
        String elementId = element.getAttributes().get("id");
        if (targetElementId.equals(elementId)) {
            return updateScriptsInElement(element, scriptIndex, newContent);
        }

        // Sinon, reconstruire l'élément avec ses enfants mis à jour
        List<Element> updatedChildren = new ArrayList<>();
        for (Element child : element.getChildren()) {
            updatedChildren.add(updateScriptInElement(child, targetElementId, scriptIndex, newContent));
        }

        // Recréer l'élément avec les enfants mis à jour
        if (element.getChildren().isEmpty()) {
            return new Element(element.getElementName(), element.getAttributes(), element.getContent());
        } else {
            return new Element(element.getElementName(), element.getAttributes(), element.getContent(), updatedChildren);
        }
    }

    private Element updateScriptsInElement(Element element, Integer scriptIndex, String newContent) {
        List<Element> scriptElements = HomemadeElementUtil.getScriptsAsElements(element);
        if (scriptIndex >= scriptElements.size()) {
            return element; // Index invalide, pas de modification
        }

        return updateElementScriptContent(element, scriptElements.get(scriptIndex), newContent);
    }

    private Element updateElementScriptContent(Element element, Element targetScript, String newContent) {
        List<Element> updatedChildren = new ArrayList<>();
        
        for (Element child : element.getChildren()) {
            if (child == targetScript) {
                // Remplacer le script avec le nouveau contenu
                updatedChildren.add(new Element(
                    child.getElementName(),
                    child.getAttributes(),
                    newContent,
                    child.getChildren()
                ));
            } else {
                // Récursivement mettre à jour les autres enfants
                updatedChildren.add(updateElementScriptContent(child, targetScript, newContent));
            }
        }

        // Recréer l'élément parent avec les enfants mis à jour
        return new Element(element.getElementName(), element.getAttributes(), element.getContent(), updatedChildren);
    }

    @Override
    public void writeToFile(File file) throws IOException {
        String xmlContent = rootElement.toXml();
        Files.writeString(file.toPath(), xmlContent);
    }
}
