package com.protectline.tojsproject.functionblock;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.jsproject.model.block.Block;
import com.protectline.jsproject.model.block.FunctionBlock;
import com.protectline.tojsproject.NodeToBlock;
import org.javatuples.Pair;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.Collection;
import java.util.List;

public class ScriptNodeToFunctionBlock implements NodeToBlock {

    @Override
    public Collection<? extends Block> getBlocks(Node node, BpmnDocument bpmnDocument, Pair<String, List<Integer>> path) {
        NamedNodeMap attributes = node.getAttributes();
        String id = path.getValue0();

        if (null != attributes && id !=null) {
            var name = getName(attributes, id);
            var script = node.getTextContent();
            return List.of(new FunctionBlock(path, name, script));
        } else {
            throw new IllegalArgumentException("Node has not attributes, could not create function block");
        }
    }

    private static String getName(NamedNodeMap attributes, String id) {
        Node nodeName;
        nodeName = attributes.getNamedItem("name") != null ? attributes.getNamedItem("name") : null;
        var name = nodeName != null ? nodeName.toString() : id != null ? id.toString() : null;
        return name;
    }
}
