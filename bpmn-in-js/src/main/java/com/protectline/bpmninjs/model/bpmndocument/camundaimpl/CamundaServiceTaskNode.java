package com.protectline.bpmninjs.model.bpmndocument.camundaimpl;

import com.protectline.bpmninjs.model.bpmndocument.api.model.ServiceTaskNode;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

public class CamundaServiceTaskNode extends CamundaNode implements ServiceTaskNode {
    public CamundaServiceTaskNode(ModelElementInstance element) {
        super(element);
    }
}
