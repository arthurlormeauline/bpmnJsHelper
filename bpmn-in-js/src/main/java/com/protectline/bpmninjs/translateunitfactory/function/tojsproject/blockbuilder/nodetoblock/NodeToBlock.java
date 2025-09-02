package com.protectline.bpmninjs.translateunitfactory.function.tojsproject.blockbuilder.nodetoblock;

import com.protectline.bpmninjs.bpmndocument.model.BpmnPath;
import com.protectline.bpmninjs.common.block.Block;

import java.util.Collection;

public interface NodeToBlock {
    Collection<? extends Block> getBlocks(BpmnPath path);
}
