package com.protectline.bpmninjs.application.function.tojsproject.blockfrombpmnnode.nodetoblock;

import com.protectline.bpmninjs.model.bpmndocument.api.model.BpmnPath;
import com.protectline.bpmninjs.model.block.Block;

import java.util.Collection;

public interface NodeToBlock {
    Collection<? extends Block> getBlocks(BpmnPath path);
}
