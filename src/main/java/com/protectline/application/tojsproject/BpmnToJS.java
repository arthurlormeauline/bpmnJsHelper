package com.protectline.application.tojsproject;

import com.protectline.application.tojsproject.blockstojsproject.FromBlockToJsProject;
import com.protectline.application.tojsproject.bpmntoblocks.FromBpmnToBlocks;
import com.protectline.files.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

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
