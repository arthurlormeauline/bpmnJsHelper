package com.protectline.bpmninjs.engine.tojsproject.spi;

import com.protectline.bpmninjs.model.bpmndocument.api.BpmnDocument;
import com.protectline.bpmninjs.model.common.block.Block;

import java.util.List;

public interface BlockBuilder {

    List<Block> getBlocks(BpmnDocument document);
}
