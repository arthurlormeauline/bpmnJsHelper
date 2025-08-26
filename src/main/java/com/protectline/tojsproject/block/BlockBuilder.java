package com.protectline.tojsproject.block;

import com.protectline.bpmndocument.model.camundaadapter.BpmnCamundaDocument;
import com.protectline.jsproject.model.block.Block;

import java.util.List;

public interface BlockBuilder {

    List<Block> getBlocks(BpmnCamundaDocument document);
}
