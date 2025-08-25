package com.protectline.tojsproject.blockbuilder;

import com.protectline.jsproject.model.block.Block;
import org.javatuples.Pair;

import java.util.List;

public class ScriptBlock extends Block {
    String script;

    public ScriptBlock(Pair<String, List<Integer>> path, String name, String script){
        super(path, name);
        this.script= script;
    }
}
