package com.protectline.bpmninjs.model.bpmndocument.camundaimpl;

import org.camunda.bpm.model.xml.instance.DomElement;

import java.util.ArrayList;
import java.util.List;

public class DomElementUtil {

    public static List<String> getScripts(DomElement input) {
        return getScriptsAsDomElement(input).stream()
                .map(element -> element.getTextContent())
                .toList();
    }

    public static List<DomElement> getScriptsAsDomElement(DomElement element) {
        List<DomElement> result = new ArrayList<>();
        recSearch(result, element);
        return result;
    }

    private static void recSearch(List<DomElement> acc, DomElement element) {
        List<DomElement> children = element.getChildElements();

        acc.addAll(children.stream()
                .filter(domElement -> domElement.getLocalName().equals("script"))
                .toList());

        for (DomElement child : children) {
            recSearch(acc, child);
        }
    }
}
