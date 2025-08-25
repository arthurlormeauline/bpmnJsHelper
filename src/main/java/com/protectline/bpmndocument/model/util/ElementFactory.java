package com.protectline.bpmndocument.model.util;

import com.protectline.bpmndocument.model.element.Attribute;
import com.protectline.bpmndocument.model.element.Element;
import org.javatuples.Pair;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.ArrayList;

import static com.protectline.bpmndocument.model.element.ElementType.SCRIPT;

public class ElementFactory {

    public static Element toElement(Node node, NamedNodeMap attributes) {
        var element = new Element();
        element.setContent(node.getTextContent());
        var elementAttributes = new ArrayList<Attribute>();
        for (int i = 0 ; i < attributes.getLength() ; i++){
            var attribute = attributes.item(i);
            var elementAttribute = new Attribute(attribute.getNodeName(), attribute.getNodeValue());
            elementAttributes.add(elementAttribute);
        }
        element.setAttributes(elementAttributes);
        return element;
    }
}
