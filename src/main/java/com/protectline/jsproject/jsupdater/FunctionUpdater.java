package com.protectline.jsproject.jsupdater;

import com.protectline.common.block.Block;
import com.protectline.common.block.BlockType;
import com.protectline.common.block.FunctionBlock;
import com.protectline.jsproject.JsUpdater;
import com.protectline.jsproject.updatertemplate.JsUpdaterTemplate;

import java.util.List;

public class FunctionUpdater implements JsUpdater {

    private final JsUpdaterTemplate template;

    public FunctionUpdater(JsUpdaterTemplate template) {
        this.template = template;
    }

    @Override
    public String update(String input, List<Block> blocks) {
        var functions = buildFunctions(blocks);
        return input.replaceAll(template.getFlag(), functions);
    }

    private String buildFunctions(List<Block> blocks) {
        StringBuilder allFunction = new StringBuilder();
        blocks.forEach(block ->
        {
            if (block.getType().equals(BlockType.FUNCTION)) {
                var function = buildFunction((FunctionBlock) block);
                allFunction.append(function);
            }
        });

        return allFunction.toString();
    }

    private String buildFunction(FunctionBlock block) {
        var function = template.getTemplate();
        function = function.replaceAll("\\*\\*id\\*\\*", block.getId().toString());
        function = function.replaceAll("\\*\\*name\\*\\*", block.getName().toString());
        function = function.replaceAll("\\*\\*content\\*\\*", block.getContent().toString());
        return function;
    }
}
