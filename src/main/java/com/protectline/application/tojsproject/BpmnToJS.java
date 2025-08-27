package com.protectline.application.tojsproject;

import com.protectline.application.tojsproject.blockstojsproject.FromBlockToJsProject;
import com.protectline.application.tojsproject.bpmntoblocks.FromBpmnToBlocks;

import java.io.IOException;
import java.nio.file.Path;

public class BpmnToJS {

    private final FromBpmnToBlocks bpmnToBlock;
    private final FromBlockToJsProject blockToJs;

    public BpmnToJS(Path workingDirectory) throws IOException {
        bpmnToBlock = new FromBpmnToBlocks(workingDirectory);
        blockToJs = new FromBlockToJsProject(workingDirectory);
    }

    public void createProject(String process) throws IOException {
        bpmnToBlock.createBlocksFromBpmn(process);
        blockToJs.updateJsProjectFromBlocks(process);
    }
}
