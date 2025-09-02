package com.protectline.bpmninjs.translateunitfactory.function.tojsproject.blockbuilder.nodetoblock.impl;

import com.protectline.bpmninjs.translateunitfactory.function.tojsproject.blockbuilder.nodetoblock.NodeToBlock;
import com.protectline.bpmninjs.bpmndocument.model.BpmnPath;
import com.protectline.bpmninjs.bpmndocument.model.Node;
import com.protectline.bpmninjs.bpmndocument.model.ServiceTaskNode;
import com.protectline.bpmninjs.common.block.Block;

import java.util.Collection;

import static com.protectline.bpmninjs.translateunitfactory.function.tojsproject.blockbuilder.nodetoblock.impl.BlockUtil.getBlockFromNode;

public class ServiceTaskNodeToFunctionBlock implements NodeToBlock {
    ServiceTaskNode node;

    public ServiceTaskNodeToFunctionBlock(Node node) {
        this.node = (ServiceTaskNode) node;
    }

    @Override
    public Collection<? extends Block> getBlocks(BpmnPath path) {
        return getBlockFromNode(node, path);
    }
}
