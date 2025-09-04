package com.protectline.bpmninjs.model.bpmndocument.homemadeimpl;

import com.protectline.bpmninjs.model.bpmndocument.api.model.Node;
import com.protectline.bpmninjs.model.bpmndocument.api.model.NodeType;
import com.protectline.bpmninjs.xmlparser.parser.Element;

import java.util.List;

public class HomemadeNode implements Node {
    protected Element element;

    public HomemadeNode(Element element) {
        this.element = element;
    }

    @Override
    public String getNodeName() {
        // Extraire le nom local du elementName (ex: "bpmn:startEvent" -> "startEvent")
        String elementName = element.getElementName();
        if (elementName.contains(":")) {
            return elementName.substring(elementName.indexOf(":") + 1);
        }
        return elementName;
    }

    @Override
    public String getAttributeValue(String attributeName) {
        String value = element.getAttributes().get(attributeName);
        // Supprimer les guillemets des valeurs d'attributs si présents
        if (value != null && value.length() >= 2 && value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

    @Override
    public List<String> getScripts() {
        return HomemadeElementUtil.getScripts(element);
    }

    @Override
    public NodeType getType() {
        String nodeName = getNodeName();
        switch (nodeName) {
            case "startEvent":
                return NodeType.START;
            case "serviceTask":
                return NodeType.SERVICE_TASK;
            case "scriptTask":
                return NodeType.SCRIPT;
            default:
                throw new IllegalArgumentException("Type not support : " + nodeName);
        }
    }
}
