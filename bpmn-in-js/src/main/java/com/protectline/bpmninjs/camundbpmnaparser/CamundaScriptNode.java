package com.protectline.bpmninjs.camundbpmnaparser;

import com.protectline.bpmninjs.bpmndocument.model.ScriptNode;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

public class CamundaScriptNode extends CamundaNode implements ScriptNode {
    public CamundaScriptNode(ModelElementInstance element) {
        super(element);
    }
}
