package com.protectline.bpmndocument;

import com.protectline.bpmndocument.model.element.Element;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.javatuples.Pair;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.protectline.bpmndocument.model.element.ElementType.SCRIPT;
import static com.protectline.bpmndocument.model.util.ElementFactory.toElement;

public class BpmnDocument {
    private BpmnModelInstance modelInstance = null;

    public BpmnDocument(Path workingDirectory, String processName) {
        File processFile = workingDirectory.resolve("input").resolve(processName + ".bpmn").toFile();
        this.modelInstance = Bpmn.readModelFromFile(processFile);
    }

    public void updateBpmn(List<Element> elements) {
    }

    public List<Element> getAllScripts() {
        List<Element> scriptElements = new ArrayList<>();

        var document = modelInstance.getDocument();

        Node node = document.getDomSource().getNode();
        String id = null;
        List<Integer> path = new ArrayList<>();
        recsome(node, id, scriptElements, path);
        return scriptElements;
    }

    private void recsome(Node node, String id, List<Element> scriptElements, List<Integer> path) {
        var attributes = node.getAttributes();
        String tempId = attributes == null ? null : attributes.getNamedItem("id") == null ? null : attributes.getNamedItem("id").getNodeValue();
        System.out.println("Node id : " + tempId == null ? " null" : tempId);
        if (tempId != null && tempId.equals("Event_1u1d1qi")) {
            int test = 3;
            test++;
        }

        if (tempId != null) {
            id = tempId;
            path = new ArrayList<>();
        }

        String elementName = node.getNodeName();

        if (elementName.equals("bpmn:script") || elementName.equals("camunda:script")) {
            var element = toElement(node, attributes);
            element.setPath(new Pair<>(id, path));
            element.setType(SCRIPT);
            scriptElements.add(element);
        } else {
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                var child = children.item(i);
                var newPath = new ArrayList(path);
                newPath.add(i);
                recsome(child, id, scriptElements, newPath);
            }
        }

    }
}
