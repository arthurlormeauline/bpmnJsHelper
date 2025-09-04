package com.protectline.bpmninjs.application.function.tojsproject.blockfrombpmnnode;

import com.protectline.bpmninjs.engine.tojsproject.spi.BlockFromBpmnNode;
import com.protectline.bpmninjs.application.function.tojsproject.blockfrombpmnnode.nodetoblock.NodeToBlock;
import com.protectline.bpmninjs.application.function.tojsproject.blockfrombpmnnode.nodetoblock.ToFunctionBlockFactory;
import com.protectline.bpmninjs.model.bpmndocument.api.BpmnDocument;
import com.protectline.bpmninjs.model.bpmndocument.api.model.BpmnPath;
import com.protectline.bpmninjs.model.bpmndocument.api.model.Node;
import com.protectline.bpmninjs.model.block.Block;

import java.util.ArrayList;
import java.util.List;

public class FunctionBlockFromBpmnNode implements BlockFromBpmnNode {

    @Override
    public List<Block> getBlocks(BpmnDocument bpmnCamundaDocument) {
        List<Block> blocks = new ArrayList<>();

        List<Node> nodes = bpmnCamundaDocument.getFirstLevelElements();

        for (Node node : nodes) {
            NodeToBlock toFunctionBlock = ToFunctionBlockFactory.getNodeToFunctionBlock(node);
            String id = getId(node);
            var path = new BpmnPath(id);
            blocks.addAll(toFunctionBlock.getBlocks(path));
        }

        return blocks;
    }

    private static String getId(Node node) {
        return node.getAttributeValue("id") != null ?
                node.getAttributeValue("id")
                : null;
    }
}
