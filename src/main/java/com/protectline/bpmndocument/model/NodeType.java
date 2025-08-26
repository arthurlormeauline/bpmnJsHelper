package com.protectline.bpmndocument.model;


public enum NodeType {
    START,
    SERVICE_TASK,
    SCRIPT;

    public static NodeType getType(Node node) {
        String nodeName = node.getNodeName().toString();
        switch (nodeName) {
            case "startEvent":
                return START;
            case "serviceTask":
                return SERVICE_TASK;
            case "scriptTask":
                return SCRIPT;
            default:
                throw new IllegalArgumentException("Type not support : " + nodeName);
        }
    }
}
