package com.protectline.tojsproject;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.jsproject.model.block.Block;

import java.util.List;

public interface BlockBuilder {

    List<Block> getBlocks(BpmnDocument document);
}
