package com.protectline.common.block.jsonblock;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.protectline.bpmndocument.model.BpmnPath;
import lombok.Getter;

@Getter
public class BpmnPathJson {
    @JsonProperty("id")
    private final String id;

    public BpmnPathJson(BpmnPath bpmnPath) {
        this.id = bpmnPath.getId();
    }

    @JsonCreator
    public BpmnPathJson(@JsonProperty("id") String id) {
        this.id = id;
    }

    public BpmnPath toBpmnPath() {
        return new BpmnPath(id);
    }
}