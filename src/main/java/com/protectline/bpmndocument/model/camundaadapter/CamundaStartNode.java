package com.protectline.bpmndocument.model.camundaadapter;

import com.protectline.bpmndocument.model.StartNode;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

public class CamundaStartNode extends CamundaNode implements StartNode {
    public CamundaStartNode(ModelElementInstance element) {
        super(element);
    }
}
