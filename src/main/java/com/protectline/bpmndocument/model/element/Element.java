package com.protectline.bpmndocument.model.element;

import lombok.Getter;
import lombok.Setter;
import org.javatuples.Pair;

import java.util.List;


@Getter
@Setter
public class Element {

    private String name;
    private List<Attribute> attributes;
    private List<Element> children;
    private String content;
    private ElementType type;
    private Pair<String, List<Integer>> path;
}
