package com.protectline.common.block.jsonblock;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.protectline.common.block.FunctionBlock;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FunctionBlocksList {
    @JsonProperty("blocks")
    private final List<FunctionJsonBlock> blocks;

    public FunctionBlocksList(List<FunctionBlock> functionBlocks, int a) {
        this.blocks = functionBlocks.stream()
                .map(FunctionJsonBlock::new)
                .collect(Collectors.toList());
    }

    @JsonCreator
    public FunctionBlocksList(@JsonProperty("blocks") List<FunctionJsonBlock> blocks) {
        this.blocks = blocks;
    }

    public List<FunctionBlock> toFunctionBlocks() {
        return blocks.stream()
                .map(FunctionJsonBlock::toFunctionBlock)
                .collect(Collectors.toList());
    }
}
