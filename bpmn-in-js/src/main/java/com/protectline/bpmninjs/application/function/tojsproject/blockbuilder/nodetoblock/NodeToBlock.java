package com.protectline.bpmninjs.application.function.tojsproject.blockbuilder.nodetoblock;

import com.protectline.bpmninjs.model.bpmndocument.api.model.BpmnPath;
import com.protectline.bpmninjs.model.common.block.Block;

import java.util.Collection;

public interface NodeToBlock {
    Collection<? extends Block> getBlocks(BpmnPath path);
}
