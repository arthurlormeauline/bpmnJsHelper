package com.protectline.jsproject.model.block;

import org.javatuples.Pair;

import java.util.List;

public class Block {
    private final String name;
    private final Pair<String, List<Integer>> path;

    public Block(Pair<String, List<Integer>> path, String name) {
        this.path = path;
        this.name = name;
    }

}
