package com.protectline.bpmndocument.model;

import org.w3c.dom.Node;

public enum NodeType {
    START,
    SERVICE_TASK,
    SCRIPT;

    public static NodeType getType(Node node) {
        String nodeName = node.getNodeName().toString();
        switch (nodeName) {
            case "bpmn:startEvent":
                return START;
            case "bpmn:serviceTask":
                return SERVICE_TASK;
            case "bpmn:scriptTask":
                return SCRIPT;
            default:
                throw new IllegalArgumentException("Type not support : " + nodeName);
        }
    }
}
