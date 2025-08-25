package com.protectline.bpmninjs.application.tojsproject;

import com.protectline.bpmninjs.application.tojsproject.blockstojsproject.FromBlockToJsProject;
import com.protectline.bpmninjs.application.tojsproject.bpmntoblocks.FromBpmnToBlocks;
import com.protectline.bpmninjs.files.FileUtil;

import java.io.IOException;

public class BpmnToJS {

    private final FromBpmnToBlocks bpmnToBlock;
    private final FromBlockToJsProject blockToJs;

    public BpmnToJS(FileUtil fileUtil) throws IOException {
        bpmnToBlock = new FromBpmnToBlocks(fileUtil);
        blockToJs = new FromBlockToJsProject(fileUtil);
    }

    public void createProject(String process) throws IOException {
        bpmnToBlock.createBlocksFromBpmn(process);
        blockToJs.updateJsProjectFromBlocks(process);
    }
}
