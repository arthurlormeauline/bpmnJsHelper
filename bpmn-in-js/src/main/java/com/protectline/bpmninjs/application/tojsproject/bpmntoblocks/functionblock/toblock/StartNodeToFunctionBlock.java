package com.protectline.bpmninjs.application.tojsproject.bpmntoblocks.functionblock.toblock;

import com.protectline.bpmninjs.application.tojsproject.bpmntoblocks.NodeToBlock;
import com.protectline.bpmninjs.bpmndocument.BpmnDocument;
import com.protectline.bpmninjs.bpmndocument.model.BpmnPath;
import com.protectline.bpmninjs.bpmndocument.model.Node;
import com.protectline.bpmninjs.bpmndocument.model.StartNode;
import com.protectline.bpmninjs.common.block.Block;

import java.util.Collection;

import static com.protectline.bpmninjs.application.tojsproject.bpmntoblocks.functionblock.toblock.FunctionBlockUtil.getFunctionBlockFromScripts;


public class StartNodeToFunctionBlock implements NodeToBlock {
    StartNode node;

    public StartNodeToFunctionBlock(Node node) {
        this.node = (StartNode) node;
    }

    @Override
    public Collection<? extends Block> getBlocks(BpmnDocument bpmnCamundaDocument, BpmnPath path) {
        return getFunctionBlockFromScripts(node, path);
    }
}
