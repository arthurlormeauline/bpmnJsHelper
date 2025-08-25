package com.protectline.tojsproject.blockbuilder;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.bpmndocument.model.element.Element;
import com.protectline.jsproject.model.block.Block;

import java.util.Collection;
import java.util.List;

public interface ToBlock {
    Collection<? extends Block> getBlocks(Element scriptElement, BpmnDocument bpmnDocument);
}
