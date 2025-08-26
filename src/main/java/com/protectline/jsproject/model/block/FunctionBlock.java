package com.protectline.jsproject.model.block;

import org.javatuples.Pair;

import java.util.List;

import static com.protectline.jsproject.model.block.BlockType.FUNCTION;

public class FunctionBlock extends Block {
    String content;

    public FunctionBlock(Pair<String, List<Integer>> path, String name, String content) {
        super(path, name, FUNCTION);
        this.content = content;
    }
}
