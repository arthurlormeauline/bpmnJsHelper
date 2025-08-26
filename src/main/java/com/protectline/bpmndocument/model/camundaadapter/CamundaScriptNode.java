package com.protectline.bpmndocument.model.camundaadapter;

import com.protectline.bpmndocument.model.ScriptNode;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

public class CamundaScriptNode extends CamundaNode implements ScriptNode {
    public CamundaScriptNode(ModelElementInstance element) {
        super(element);
    }
}
