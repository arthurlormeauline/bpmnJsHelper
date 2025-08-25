package com.protectline.tojsproject.blockbuilder;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.bpmndocument.model.element.Element;
import com.protectline.jsproject.model.block.Block;

import java.util.ArrayList;
import java.util.List;

public class BlockBuilder {
    
    private final BpmnDocument bpmnDocument;
    
    public BlockBuilder(BpmnDocument bpmnDocument) {
        this.bpmnDocument = bpmnDocument;
    }
    
    public List<Block> buildAllBlocks() {
        List<Block> blocks = new ArrayList<>();

        blocks.addAll(getScriptBlock());

        return blocks;
    }

    private List<Block> getScriptBlock(){
        List<Element> scriptElements = bpmnDocument.getAllScripts();
        List<Block> blocks = new ArrayList<>();

        for (Element scriptElement : scriptElements){
            ToBlock toBlock = ToBlockFactory.getToBlock(scriptElement);
            blocks.addAll(toBlock.getBlocks(scriptElement, bpmnDocument));
        }

        return blocks;
    }
}
