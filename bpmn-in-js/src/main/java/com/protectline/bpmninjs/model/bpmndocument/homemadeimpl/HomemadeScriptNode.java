package com.protectline.bpmninjs.model.bpmndocument.homemadeimpl;

import com.protectline.bpmninjs.model.bpmndocument.api.model.ScriptNode;
import com.protectline.bpmninjs.xmlparser.Element;

public class HomemadeScriptNode extends HomemadeNode implements ScriptNode {

    public HomemadeScriptNode(Element element) {
        super(element);
    }
}