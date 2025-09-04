package com.protectline.bpmninjs.engine.tojsproject;

import com.protectline.bpmninjs.model.block.persist.BlockUtil;
import com.protectline.bpmninjs.engine.mainfactory.MainFactory;
import com.protectline.bpmninjs.engine.tojsproject.blockstojsproject.FromBlockToJsProject;
import com.protectline.bpmninjs.engine.tojsproject.bpmntoblocks.FromBpmnToBlocks;
import com.protectline.bpmninjs.engine.files.FileService;

import java.io.IOException;

public class BpmnToJsProject {

    private final FromBpmnToBlocks bpmnToBlock;
    private final FromBlockToJsProject blockToJs;

    public BpmnToJsProject(FileService fileService, MainFactory mainFactory) throws IOException {
        bpmnToBlock = new FromBpmnToBlocks(fileService, mainFactory.getBlockBuilders(), new BlockUtil());
        blockToJs = new FromBlockToJsProject(fileService, new BlockUtil(), mainFactory);
    }

    public void createProject(String process) throws IOException {
        bpmnToBlock.createBlocksFromBpmn(process);
        blockToJs.updateJsProjectFromBlocks(process);
    }
}
