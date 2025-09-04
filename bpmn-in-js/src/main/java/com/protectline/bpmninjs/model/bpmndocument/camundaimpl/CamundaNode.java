package com.protectline.bpmninjs.model.bpmndocument.camundaimpl;


import com.protectline.bpmninjs.model.bpmndocument.api.model.Node;
import com.protectline.bpmninjs.model.bpmndocument.api.model.NodeType;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import java.util.List;

import static com.protectline.bpmninjs.model.bpmndocument.api.model.NodeType.*;


public class CamundaNode implements Node {
    ModelElementInstance element;

    public CamundaNode(ModelElementInstance element) {
        this.element = element;
    }

    @Override
    public String getNodeName() {
        return element.getElementType().getTypeName().toString();
    }

    @Override
    public String getAttributeValue(String id) {
        return element.getAttributeValue(id);
    }

    @Override
    public List<String> getScripts() {
        return DomElementUtil.getScripts(element.getDomElement());
    }

    @Override
    public NodeType getType() {
        String nodeName = getNodeName().toString();
        switch (nodeName) {
            case "startEvent":
                return START;
            case "serviceTask":
                return SERVICE_TASK;
            case "scriptTask":
                return SCRIPT;
            default:
                throw new IllegalArgumentException("Type not support : " + nodeName);
        }
    }
}
