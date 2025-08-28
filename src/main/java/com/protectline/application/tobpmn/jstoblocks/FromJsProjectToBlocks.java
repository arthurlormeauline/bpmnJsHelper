package com.protectline.application.tobpmn.jstoblocks;

import com.protectline.files.FileUtil;

public class FromJsProjectToBlocks {
    private final FileUtil fileUtil;

    public FromJsProjectToBlocks(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    public void updateBlockFromJsProject(String process){
        // get blocks : with fileUtil and readBlocksFrom file ; should be present
        // parse js project : build all blocks (let implementation for latter for now)
        // compare blocks from file, from blocks from js project, should have same number of block with same Id's
        // for all block from js project, use a factory based on block type to get a BlockUpdaterFromJs object that take as parameter : blocks from file, and given block from js project. For "FUNCTION" block the FunctionBlockUpdaterFromJs project implementation would replace the content of the block from file with uuid equals uuid from the given block with the content of the given block from js project
    }
}
