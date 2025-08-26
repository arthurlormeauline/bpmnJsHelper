package com.protectline.tojsproject.block.functionblock.toblock;

import com.protectline.bpmndocument.model.BpmnPath;
import com.protectline.bpmndocument.model.Node;
import com.protectline.bpmndocument.model.StartNode;
import com.protectline.bpmndocument.model.camundaadapter.BpmnCamundaDocument;
import com.protectline.jsproject.model.block.Block;
import com.protectline.tojsproject.block.NodeToBlock;

import java.util.Collection;

import static com.protectline.tojsproject.block.functionblock.toblock.FunctionBlockUtil.getFunctionBlockFromScripts;


public class StartNodeToFunctionBlock implements NodeToBlock {
    StartNode node;

    public StartNodeToFunctionBlock(Node node) {
        this.node = (StartNode) node;
    }

    @Override
    public Collection<? extends Block> getBlocks(BpmnCamundaDocument bpmnCamundaDocument, BpmnPath path) {
        return getFunctionBlockFromScripts(node, path);
    }
}
