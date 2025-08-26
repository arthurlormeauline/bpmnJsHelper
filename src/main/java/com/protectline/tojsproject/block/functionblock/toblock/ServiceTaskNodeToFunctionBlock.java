package com.protectline.tojsproject.block.functionblock.toblock;

import com.protectline.bpmndocument.model.ServiceTaskNode;
import com.protectline.bpmndocument.model.camundaadapter.BpmnCamundaDocument;
import com.protectline.bpmndocument.model.BpmnPath;
import com.protectline.bpmndocument.model.Node;
import com.protectline.jsproject.model.block.Block;
import com.protectline.jsproject.model.block.FunctionBlock;
import com.protectline.tojsproject.block.NodeToBlock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.protectline.tojsproject.block.functionblock.toblock.FunctionBlockUtil.getFunctionBlockFromScripts;

public class ServiceTaskNodeToFunctionBlock implements NodeToBlock {
    ServiceTaskNode node;

    public ServiceTaskNodeToFunctionBlock(Node node) {
        this.node = (ServiceTaskNode) node;
    }

    @Override
    public Collection<? extends Block> getBlocks(BpmnCamundaDocument bpmnCamundaDocument, BpmnPath path) {
        return getFunctionBlockFromScripts(node, path);
    }
}
