package com.protectline.bpmninjs.application;

import com.protectline.bpmninjs.application.tojsproject.bpmntoblocks.BlockBuilder;
import com.protectline.bpmninjs.application.tojsproject.bpmntoblocks.functionblock.FunctionBlockBuilder;

import java.util.List;

public class BuildersProvider {
    public List<BlockBuilder> getBuilders() {
        return List.of(new FunctionBlockBuilder());
    }
}
