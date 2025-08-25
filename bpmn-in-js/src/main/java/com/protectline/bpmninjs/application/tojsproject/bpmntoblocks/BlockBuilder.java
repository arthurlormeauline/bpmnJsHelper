package com.protectline.bpmninjs.application.tojsproject.bpmntoblocks;

import com.protectline.bpmninjs.bpmndocument.BpmnDocument;
import com.protectline.bpmninjs.common.block.Block;

import java.util.List;

public interface BlockBuilder {

    List<Block> getBlocks(BpmnDocument document);
}
