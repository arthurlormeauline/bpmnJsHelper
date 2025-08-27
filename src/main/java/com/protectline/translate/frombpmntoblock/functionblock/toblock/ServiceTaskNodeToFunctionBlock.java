package com.protectline.translate.frombpmntoblock.functionblock.toblock;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.bpmndocument.model.ServiceTaskNode;
import com.protectline.bpmndocument.model.BpmnPath;
import com.protectline.bpmndocument.model.Node;
import com.protectline.common.block.Block;
import com.protectline.translate.frombpmntoblock.NodeToBlock;

import java.util.Collection;

import static com.protectline.translate.frombpmntoblock.functionblock.toblock.FunctionBlockUtil.getFunctionBlockFromScripts;

public class ServiceTaskNodeToFunctionBlock implements NodeToBlock {
    ServiceTaskNode node;

    public ServiceTaskNodeToFunctionBlock(Node node) {
        this.node = (ServiceTaskNode) node;
    }

    @Override
    public Collection<? extends Block> getBlocks(BpmnDocument bpmnCamundaDocument, BpmnPath path) {
        return getFunctionBlockFromScripts(node, path);
    }
}
