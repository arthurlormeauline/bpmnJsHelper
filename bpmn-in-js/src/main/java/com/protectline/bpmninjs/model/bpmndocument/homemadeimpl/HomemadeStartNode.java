package com.protectline.bpmninjs.model.bpmndocument.homemadeimpl;

import com.protectline.bpmninjs.model.bpmndocument.api.model.StartNode;
import com.protectline.bpmninjs.xmlparser.parser.Element;

public class HomemadeStartNode extends HomemadeNode implements StartNode {

    public HomemadeStartNode(Element element) {
        super(element);
    }
}
