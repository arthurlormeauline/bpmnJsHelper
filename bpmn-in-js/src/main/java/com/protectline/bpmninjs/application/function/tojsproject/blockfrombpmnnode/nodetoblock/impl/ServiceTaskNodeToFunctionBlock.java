package com.protectline.bpmninjs.application.function.tojsproject.blockfrombpmnnode.nodetoblock.impl;

import com.protectline.bpmninjs.application.function.tojsproject.blockfrombpmnnode.nodetoblock.NodeToBlock;
import com.protectline.bpmninjs.model.bpmndocument.api.model.BpmnPath;
import com.protectline.bpmninjs.model.bpmndocument.api.model.Node;
import com.protectline.bpmninjs.model.bpmndocument.api.model.ServiceTaskNode;
import com.protectline.bpmninjs.model.block.Block;

import java.util.Collection;

import static com.protectline.bpmninjs.application.function.tojsproject.blockfrombpmnnode.nodetoblock.impl.BlockUtil.getBlockFromNode;

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
