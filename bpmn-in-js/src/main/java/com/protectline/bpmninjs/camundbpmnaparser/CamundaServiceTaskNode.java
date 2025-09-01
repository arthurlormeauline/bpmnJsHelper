package com.protectline.bpmninjs.camundbpmnaparser;

import com.protectline.bpmninjs.bpmndocument.model.ServiceTaskNode;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

public class CamundaServiceTaskNode extends CamundaNode implements ServiceTaskNode {
    public CamundaServiceTaskNode(ModelElementInstance element) {
        super(element);
    }
}
