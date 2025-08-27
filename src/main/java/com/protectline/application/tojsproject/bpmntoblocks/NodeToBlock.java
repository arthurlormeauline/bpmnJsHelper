package com.protectline.application.tojsproject.bpmntoblocks;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.bpmndocument.model.BpmnPath;
import com.protectline.common.block.Block;

import java.util.Collection;

public interface NodeToBlock {
    Collection<? extends Block> getBlocks(BpmnDocument document, BpmnPath path);
}
