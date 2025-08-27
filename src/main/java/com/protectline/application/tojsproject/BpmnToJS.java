package com.protectline.application.tojsproject;

import com.protectline.application.tojsproject.blockstojsproject.FromBlockToJsProject;
import com.protectline.application.tojsproject.bpmntoblocks.FromBpmnToBlock;

import java.io.IOException;
import java.nio.file.Path;

public class BpmnToJS {

    private final FromBpmnToBlock bpmnToBlock;
    private final FromBlockToJsProject blockToJs;

    public BpmnToJS(Path workingDirectory) throws IOException {
        bpmnToBlock = new FromBpmnToBlock(workingDirectory);
        blockToJs = new FromBlockToJsProject(workingDirectory);
    }

    public void createProject(String process) throws IOException {
        bpmnToBlock.createBlocksFromBpmn(process);
        blockToJs.updateJsProjectFromBlocks(process);
    }
}
