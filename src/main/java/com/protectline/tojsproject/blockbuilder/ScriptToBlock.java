package com.protectline.tojsproject.blockbuilder;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.bpmndocument.model.element.Element;
import com.protectline.jsproject.model.block.Block;

import java.util.Collection;
import java.util.List;

public class ScriptToBlock implements ToBlock {

    @Override
    public Collection<? extends Block> getBlocks(Element scriptElement, BpmnDocument bpmnDocument) {
        var path = scriptElement.getPath();
        var name = null == scriptElement.getName() ? scriptElement.getName() : scriptElement.getPath().getValue0();
        var script = scriptElement.getContent();
        return List.of(new ScriptBlock(path, name, script));
    }
}
