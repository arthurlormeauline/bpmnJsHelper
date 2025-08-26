package com.protectline.bpmndocument.model.camundaadapter;

import org.camunda.bpm.model.xml.instance.DomElement;

import java.util.ArrayList;
import java.util.List;

public class DomElementUtil {

    public static List<String> getScriptsFromDomElement(DomElement element) {
        List<String> result = new ArrayList<>();
        recSearch(result, element);
        return result;
    }

    private static void recSearch(List<String> acc, DomElement element) {
        List<DomElement> children = element.getChildElements();

        acc.addAll(children.stream()
                .filter(domElement -> domElement.getLocalName().equals("script"))
                .map(domElement -> domElement.getTextContent())
                .toList());

        for (DomElement child : children) {
            recSearch(acc, child);
        }
    }
}
