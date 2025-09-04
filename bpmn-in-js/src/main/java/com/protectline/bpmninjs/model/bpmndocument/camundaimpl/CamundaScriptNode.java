package com.protectline.bpmninjs.model.bpmndocument.camundaimpl;

import com.protectline.bpmninjs.model.bpmndocument.api.model.ScriptNode;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

public class CamundaScriptNode extends CamundaNode implements ScriptNode {
    public CamundaScriptNode(ModelElementInstance element) {
        super(element);
    }
}
