package com.protectline.tojsproject.functionblock;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.jsproject.model.block.Block;
import com.protectline.tojsproject.BlockBuilder;
import com.protectline.tojsproject.NodeToBlock;
import org.javatuples.Pair;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class FunctionBlockBuilder implements BlockBuilder {

    @Override
    public List<Block> getBlocks(BpmnDocument bpmnDocument) {
        List<Block> blocks = new ArrayList<>();

        List<Node> nodes = bpmnDocument.getFirstLevelElements();

        for (Node node : nodes) {
            NodeToBlock toFunctionBlock = ToFunctionBlockFactory.getNodeToFunctionBlock(node);
            String id = getId(node);
            var path = new Pair<String, List<Integer>>(id, List.of());
            blocks.addAll(toFunctionBlock.getBlocks(node, bpmnDocument, path));
        }

        return blocks;
    }

    private static String getId(Node node) {
        return node.getAttributes() != null ?
                node.getAttributes().getNamedItem("id") != null ?
                        node.getAttributes().getNamedItem("id").getNodeValue() != null ?
                                node.getAttributes().getNamedItem("id").getNodeValue().toString()
                                : null
                        : null
                : null;
    }
}
