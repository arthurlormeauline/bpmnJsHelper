package com.protectline.bpmninjs.application.function.tobpmn.updateblock;

import com.protectline.bpmninjs.engine.tobpmn.spi.UpdateBlock;
import com.protectline.bpmninjs.model.block.Block;

import java.util.List;

public class UpdateFunctionBlocks implements UpdateBlock {

    @Override
    public void update(List<Block> blocksFromFile, Block blockFromJs) {
        Block jsBlock = (Block) blockFromJs;
        String jsBlockUuid = jsBlock.getId();

        for (Block fileBlock : blocksFromFile) {
            if (jsBlockUuid != null && jsBlockUuid.equals(fileBlock.getId())) {
                if (fileBlock instanceof Block) {
                    ((Block) fileBlock).setContent(jsBlock.getContent());
                } else {
                    throw new IllegalArgumentException("Block in block file with id : " + jsBlockUuid + " has not the same type (" + fileBlock.getType() + ") as the type of block with the same id of the js project ("+jsBlock.getType());
                }
            }
        }
    }
}
