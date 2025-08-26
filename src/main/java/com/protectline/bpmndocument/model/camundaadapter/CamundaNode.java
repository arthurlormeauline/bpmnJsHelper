package com.protectline.bpmndocument.model.camundaadapter;

import com.protectline.bpmndocument.model.Node;
import org.camunda.bpm.model.xml.instance.DomElement;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.protectline.bpmndocument.model.camundaadapter.DomElementUtil.getScriptsFromDomElement;

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
        return getScriptsFromDomElement(element.getDomElement());
    }
}
