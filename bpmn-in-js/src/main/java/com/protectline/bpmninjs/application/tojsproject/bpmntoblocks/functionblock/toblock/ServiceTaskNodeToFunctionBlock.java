package com.protectline.bpmninjs.application.tojsproject.bpmntoblocks.functionblock.toblock;

import com.protectline.bpmninjs.application.tojsproject.bpmntoblocks.NodeToBlock;
import com.protectline.bpmninjs.bpmndocument.BpmnDocument;
import com.protectline.bpmninjs.bpmndocument.model.BpmnPath;
import com.protectline.bpmninjs.bpmndocument.model.Node;
import com.protectline.bpmninjs.bpmndocument.model.ServiceTaskNode;
import com.protectline.bpmninjs.common.block.Block;

import java.util.Collection;

import static com.protectline.bpmninjs.application.tojsproject.bpmntoblocks.functionblock.toblock.FunctionBlockUtil.getFunctionBlockFromScripts;

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
