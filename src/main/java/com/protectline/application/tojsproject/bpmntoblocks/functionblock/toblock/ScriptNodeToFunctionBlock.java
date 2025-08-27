package com.protectline.application.tojsproject.bpmntoblocks.functionblock.toblock;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.bpmndocument.model.BpmnPath;
import com.protectline.bpmndocument.model.ScriptNode;
import com.protectline.bpmndocument.model.Node;
import com.protectline.common.block.Block;
import com.protectline.application.tojsproject.bpmntoblocks.NodeToBlock;

import java.util.Collection;

import static com.protectline.application.tojsproject.bpmntoblocks.functionblock.toblock.FunctionBlockUtil.getFunctionBlockFromScripts;

public class ScriptNodeToFunctionBlock implements NodeToBlock {

    ScriptNode node;

    public ScriptNodeToFunctionBlock(Node node) {
       this.node = (ScriptNode) node;
    }

    @Override
    public Collection<? extends Block> getBlocks(BpmnDocument bpmnCamundaDocument, BpmnPath path) {
      return getFunctionBlockFromScripts(node, path);
    }
}
