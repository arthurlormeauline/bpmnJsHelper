package com.protectline.application.tobpmn.jstoblocks;

import com.protectline.common.block.Block;
import com.protectline.common.block.FunctionBlock;

import java.util.List;

public class FunctionBlockUpdaterFromJs implements BlockUpdaterFromJs {

    @Override
    public void updateBlockContent(List<Block> blocksFromFile, Block blockFromJs) {
        FunctionBlock jsBlock = (FunctionBlock) blockFromJs;
        String jsBlockUuid = jsBlock.getId();

        for (Block fileBlock : blocksFromFile) {
            if (jsBlockUuid != null && jsBlockUuid.equals(fileBlock.getId())) {
                if (fileBlock instanceof FunctionBlock) {
                    ((FunctionBlock) fileBlock).setContent(jsBlock.getContent());
                } else {
                    throw new IllegalArgumentException("Block in block file with id : " + jsBlockUuid + " has not the same type (" + fileBlock.getType() + ") as the type of block with the same id of the js project ("+jsBlock.getType());
                }
            }
        }
    }
}
