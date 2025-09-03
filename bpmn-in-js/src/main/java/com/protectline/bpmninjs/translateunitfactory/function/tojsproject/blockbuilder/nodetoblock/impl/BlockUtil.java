package com.protectline.bpmninjs.translateunitfactory.function.tojsproject.blockbuilder.nodetoblock.impl;

import com.protectline.bpmninjs.bpmndocument.model.BpmnPath;
import com.protectline.bpmninjs.bpmndocument.model.Node;
import com.protectline.bpmninjs.common.block.Block;

import java.util.ArrayList;
import java.util.List;

public class BlockUtil {
    public static List<Block> getBlockFromNode(Node node, BpmnPath path) {
        String id = path.getId();

        if (id != null) {
            var name = getName(node, id);
            var scripts = node.getScripts();
            List<Block> blocks = new ArrayList<>();
            for (int i = 0; i < scripts.size(); i++) {
                var script = scripts.get(i);
                blocks.add(new Block(path, name + "_" + i, script, node.getType()));
            }
            return blocks;
        } else {
            throw new IllegalArgumentException("Node has not attributes, could not create function block");
        }
    }

    private static String getName(Node node, String id) {
        String name = node.getAttributeValue("name") != null ? node.getAttributeValue("name") : id;
        // Replace spaces with underscores for consistent naming
        return name.replace(" ", "_");
    }

}
