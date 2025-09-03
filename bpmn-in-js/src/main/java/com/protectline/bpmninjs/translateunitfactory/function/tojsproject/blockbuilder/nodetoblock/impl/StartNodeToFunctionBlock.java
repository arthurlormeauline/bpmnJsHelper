package com.protectline.bpmninjs.translateunitfactory.function.tojsproject.blockbuilder.nodetoblock.impl;

import com.protectline.bpmninjs.translateunitfactory.function.tojsproject.blockbuilder.nodetoblock.NodeToBlock;
import com.protectline.bpmninjs.bpmndocument.model.BpmnPath;
import com.protectline.bpmninjs.bpmndocument.model.Node;
import com.protectline.bpmninjs.bpmndocument.model.StartNode;
import com.protectline.bpmninjs.common.block.Block;

import java.util.Collection;

import static com.protectline.bpmninjs.translateunitfactory.function.tojsproject.blockbuilder.nodetoblock.impl.BlockUtil.getBlockFromNode;


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
