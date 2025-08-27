package com.protectline.camundbpmnaparser;

import com.protectline.bpmndocument.model.ServiceTaskNode;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

public class CamundaServiceTaskNode extends CamundaNode implements ServiceTaskNode {
    public CamundaServiceTaskNode(ModelElementInstance element) {
        super(element);
    }
}
