package com.protectline.jsproject.model.block;

import lombok.Getter;
import org.javatuples.Pair;

import java.util.List;

@Getter
public class Block {
    private final String name;
    private final Pair<String, List<Integer>> path;
    private final BlockType type;

    public Block(Pair<String, List<Integer>> path, String name, BlockType type) {
        this.path = path;
        this.name = name;
        this.type = type;
    }
}
