package com.protectline.bpmninjs.application.tojsproject.bpmntoblocks.functionblock.toblock;

import com.protectline.bpmninjs.application.tojsproject.bpmntoblocks.NodeToBlock;
import com.protectline.bpmninjs.bpmndocument.BpmnDocument;
import com.protectline.bpmninjs.bpmndocument.model.BpmnPath;
import com.protectline.bpmninjs.bpmndocument.model.Node;
import com.protectline.bpmninjs.bpmndocument.model.ScriptNode;
import com.protectline.bpmninjs.common.block.Block;

import java.util.Collection;

import static com.protectline.bpmninjs.application.tojsproject.bpmntoblocks.functionblock.toblock.BlockUtil.getBlockFromScripts;

public class ScriptNodeToFunctionBlock implements NodeToBlock {

    ScriptNode node;

    public ScriptNodeToFunctionBlock(Node node) {
       this.node = (ScriptNode) node;
    }

    @Override
    public Collection<? extends Block> getBlocks(BpmnDocument bpmnCamundaDocument, BpmnPath path) {
      return getBlockFromScripts(node, path);
    }
}
