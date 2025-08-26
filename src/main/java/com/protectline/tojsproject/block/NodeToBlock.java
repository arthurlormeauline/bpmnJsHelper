package com.protectline.tojsproject.block;

import com.protectline.bpmndocument.model.camundaadapter.BpmnCamundaDocument;
import com.protectline.bpmndocument.model.BpmnPath;
import com.protectline.jsproject.model.block.Block;

import java.util.Collection;

public interface NodeToBlock {
    Collection<? extends Block> getBlocks(BpmnCamundaDocument bpmnCamundaDocument, BpmnPath path);
}
