package com.protectline.bpmninjs.translateunitfactory.function.tojsproject.blockbuilder.nodetoblock.impl;

import com.protectline.bpmninjs.translateunitfactory.function.tojsproject.blockbuilder.nodetoblock.NodeToBlock;
import com.protectline.bpmninjs.bpmndocument.model.BpmnPath;
import com.protectline.bpmninjs.bpmndocument.model.Node;
import com.protectline.bpmninjs.bpmndocument.model.ScriptNode;
import com.protectline.bpmninjs.common.block.Block;

import java.util.Collection;

import static com.protectline.bpmninjs.translateunitfactory.function.tojsproject.blockbuilder.nodetoblock.impl.BlockUtil.getBlockFromNode;

public class ScriptNodeToFunctionBlock implements NodeToBlock {

    ScriptNode node;

    public ScriptNodeToFunctionBlock(Node node) {
       this.node = (ScriptNode) node;
    }

    @Override
    public Collection<? extends Block> getBlocks(BpmnPath path) {
      return getBlockFromNode(node, path);
    }
}
