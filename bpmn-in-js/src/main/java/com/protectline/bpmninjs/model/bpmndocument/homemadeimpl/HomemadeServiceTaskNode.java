package com.protectline.bpmninjs.model.bpmndocument.homemadeimpl;

import com.protectline.bpmninjs.model.bpmndocument.api.model.ServiceTaskNode;
import com.protectline.bpmninjs.xmlparser.Element;

public class HomemadeServiceTaskNode extends HomemadeNode implements ServiceTaskNode {

    public HomemadeServiceTaskNode(Element element) {
        super(element);
    }
}