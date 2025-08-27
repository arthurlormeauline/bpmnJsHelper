package com.protectline.application.tojsproject.bpmntoblocks;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.common.block.Block;

import java.util.List;

public interface BlockBuilder {

    List<Block> getBlocks(BpmnDocument document);
}
