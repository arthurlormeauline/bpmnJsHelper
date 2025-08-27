package com.protectline.application.tojsproject.bpmntoblocks.functionblock.toblock;

import com.protectline.bpmndocument.model.BpmnPath;
import com.protectline.bpmndocument.model.Node;
import com.protectline.common.block.Block;
import com.protectline.common.block.FunctionBlock;

import java.util.ArrayList;
import java.util.List;

public class FunctionBlockUtil {
    public static List<Block> getFunctionBlockFromScripts(Node node, BpmnPath path) {
        String id = path.getId();

        if (id != null) {
            var name = getName(node, id).replace(" ", "_");
            var scripts = node.getScripts();
            List<Block> blocks = new ArrayList<>();
            for (int i = 0; i < scripts.size(); i++) {
                var script = scripts.get(i);
                blocks.add(new FunctionBlock(path, name + "_" + i, script, node.getType()));
            }
            return blocks;
        } else {
            throw new IllegalArgumentException("Node has not attributes, could not create function block");
        }
    }

    private static String getName(Node node, String id) {
        return node.getAttributeValue("name") != null ? node.getAttributeValue("name") : id;
    }

}
