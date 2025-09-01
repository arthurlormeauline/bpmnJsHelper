package com.protectline.bpmninjs.camundbpmnaparser;

import com.protectline.bpmninjs.bpmndocument.model.StartNode;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

public class CamundaStartNode extends CamundaNode implements StartNode {
    public CamundaStartNode(ModelElementInstance element) {
        super(element);
    }
}
