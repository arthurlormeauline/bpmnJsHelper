package com.protectline.tojsproject.model.element;

import java.util.List;

public class BpmnElement {

    private String name;
    private List<BpmnAttribute> bpmnAttributes;
    private List<BpmnElement> children;
    String content;
}
