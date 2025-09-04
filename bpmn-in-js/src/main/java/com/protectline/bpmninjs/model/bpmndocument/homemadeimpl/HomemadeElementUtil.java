package com.protectline.bpmninjs.model.bpmndocument.homemadeimpl;

import com.protectline.bpmninjs.xmlparser.Element;

import java.util.ArrayList;
import java.util.List;

public class HomemadeElementUtil {

    /**
     * Recherche récursive d'éléments de script dans un élément BPMN
     * Recherche les éléments avec elementName="bpmn:script" ou "camunda:script"
     */
    public static List<String> getScripts(Element element) {
        return getScriptsAsElements(element).stream()
                .map(Element::getContent)
                .toList();
    }

    /**
     * Recherche récursive d'éléments de script et retourne les éléments
     */
    public static List<Element> getScriptsAsElements(Element element) {
        List<Element> result = new ArrayList<>();
        recursiveScriptSearch(result, element);
        return result;
    }

    private static void recursiveScriptSearch(List<Element> acc, Element element) {
        List<Element> children = element.getChildren();

        // Rechercher les éléments script (bpmn:script ou camunda:script)
        acc.addAll(children.stream()
                .filter(child -> isScriptElement(child.getElementName()))
                .toList());

        // Recherche récursive dans tous les enfants
        for (Element child : children) {
            recursiveScriptSearch(acc, child);
        }
    }

    private static boolean isScriptElement(String elementName) {
        return "bpmn:script".equals(elementName) || "camunda:script".equals(elementName);
    }

    /**
     * Recherche d'éléments par nom d'élément (remplace getModelElementInstance)
     */
    public static List<Element> getElementsByElementName(Element rootElement, String targetElementName) {
        List<Element> result = new ArrayList<>();
        recursiveElementSearch(result, rootElement, targetElementName);
        return result;
    }

    private static void recursiveElementSearch(List<Element> acc, Element element, String targetElementName) {
        // Si l'élément courant correspond, l'ajouter
        if (targetElementName.equals(element.getElementName())) {
            acc.add(element);
        }

        // Recherche récursive dans tous les enfants
        for (Element child : element.getChildren()) {
            recursiveElementSearch(acc, child, targetElementName);
        }
    }

    /**
     * Recherche d'un élément par ID
     */
    public static Element getElementById(Element rootElement, String id) {
        if (id.equals(rootElement.getAttributes().get("id"))) {
            return rootElement;
        }

        for (Element child : rootElement.getChildren()) {
            Element found = getElementById(child, id);
            if (found != null) {
                return found;
            }
        }

        return null;
    }
}