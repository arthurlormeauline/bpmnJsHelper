package com.protectline.tojsproject.block.functionblock;

import com.protectline.bpmndocument.model.camundaadapter.BpmnCamundaDocument;
import com.protectline.bpmndocument.model.BpmnPath;
import com.protectline.bpmndocument.model.Node;
import com.protectline.jsproject.model.block.Block;
import com.protectline.tojsproject.block.BlockBuilder;
import com.protectline.tojsproject.block.NodeToBlock;

import java.util.ArrayList;
import java.util.List;

public class FunctionBlockBuilder implements BlockBuilder {

    @Override
    public List<Block> getBlocks(BpmnCamundaDocument bpmnCamundaDocument) {
        List<Block> blocks = new ArrayList<>();

        List<Node> nodes = bpmnCamundaDocument.getFirstLevelElements();

        for (Node node : nodes) {
            NodeToBlock toFunctionBlock = ToFunctionBlockFactory.getNodeToFunctionBlock(node);
            String id = getId(node);
            var path = new BpmnPath(id);
            blocks.addAll(toFunctionBlock.getBlocks(bpmnCamundaDocument, path));
        }

        return blocks;
    }

    private static String getId(Node node) {
        return node.getAttributeValue("id") != null ?
                node.getAttributeValue("id")
                : null;
    }
}
