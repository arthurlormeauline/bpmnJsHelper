package com.protectline.bpmninjs.engine.tojsproject.spi;

import com.protectline.bpmninjs.model.bpmndocument.api.BpmnDocument;
import com.protectline.bpmninjs.model.block.Block;

import java.util.List;

public interface BlockFromBpmnNode {

    List<Block> getBlocks(BpmnDocument document);
}
