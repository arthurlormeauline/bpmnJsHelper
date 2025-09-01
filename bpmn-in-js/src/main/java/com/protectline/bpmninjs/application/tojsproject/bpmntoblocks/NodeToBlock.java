package com.protectline.bpmninjs.application.tojsproject.bpmntoblocks;

import com.protectline.bpmninjs.bpmndocument.BpmnDocument;
import com.protectline.bpmninjs.bpmndocument.model.BpmnPath;
import com.protectline.bpmninjs.common.block.Block;

import java.util.Collection;

public interface NodeToBlock {
    Collection<? extends Block> getBlocks(BpmnDocument document, BpmnPath path);
}
