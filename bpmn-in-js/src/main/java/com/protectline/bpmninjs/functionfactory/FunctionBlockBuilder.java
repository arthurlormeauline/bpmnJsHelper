package com.protectline.bpmninjs.functionfactory;

import com.protectline.bpmninjs.application.tojsproject.bpmntoblocks.BlockBuilder;
import com.protectline.bpmninjs.functionfactory.functionblock.NodeToBlock;
import com.protectline.bpmninjs.functionfactory.functionblock.ToFunctionBlockFactory;
import com.protectline.bpmninjs.bpmndocument.BpmnDocument;
import com.protectline.bpmninjs.bpmndocument.model.BpmnPath;
import com.protectline.bpmninjs.bpmndocument.model.Node;
import com.protectline.bpmninjs.common.block.Block;

import java.util.ArrayList;
import java.util.List;

public class FunctionBlockBuilder implements BlockBuilder {

    @Override
    public List<Block> getBlocks(BpmnDocument bpmnCamundaDocument) {
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
