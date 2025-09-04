package com.protectline.bpmninjs.model.bpmndocument.camundaimpl;

import com.protectline.bpmninjs.model.bpmndocument.api.model.StartNode;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

public class CamundaStartNode extends CamundaNode implements StartNode {
    public CamundaStartNode(ModelElementInstance element) {
        super(element);
    }
}
