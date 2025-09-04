package com.protectline.bpmninjs.application.function.tojsproject.blockfrombpmnnode.nodetoblock.impl;

import com.protectline.bpmninjs.application.function.tojsproject.blockfrombpmnnode.nodetoblock.NodeToBlock;
import com.protectline.bpmninjs.model.bpmndocument.api.model.BpmnPath;
import com.protectline.bpmninjs.model.bpmndocument.api.model.Node;
import com.protectline.bpmninjs.model.bpmndocument.api.model.StartNode;
import com.protectline.bpmninjs.model.block.Block;

import java.util.Collection;

import static com.protectline.bpmninjs.application.function.tojsproject.blockfrombpmnnode.nodetoblock.impl.BlockUtil.getBlockFromNode;


public class StartNodeToFunctionBlock implements NodeToBlock {
    StartNode node;

    public StartNodeToFunctionBlock(Node node) {
        this.node = (StartNode) node;
    }

    @Override
    public Collection<? extends Block> getBlocks(BpmnPath path) {
        return getBlockFromNode(node, path);
    }
}
