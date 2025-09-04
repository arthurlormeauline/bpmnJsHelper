package com.protectline.bpmninjs.model.block.persist;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.protectline.bpmninjs.model.block.Block;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FunctionBlocksList {
    @JsonProperty("blocks")
    private final List<JsonBlock> blocks;

    public FunctionBlocksList(List<Block> blocks, int a) {
        this.blocks = blocks.stream()
                .map(JsonBlock::new)
                .collect(Collectors.toList());
    }

    @JsonCreator
    public FunctionBlocksList(@JsonProperty("blocks") List<JsonBlock> blocks) {
        this.blocks = blocks;
    }

    public List<Block> toBlocks() {
        return blocks.stream()
                .map(JsonBlock::toBlock)
                .collect(Collectors.toList());
    }

    // Backward compatibility method
    public List<Block> toFunctionBlocks() {
        return toBlocks();
    }
}
