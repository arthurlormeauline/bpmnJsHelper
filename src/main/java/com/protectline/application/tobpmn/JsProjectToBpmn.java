package com.protectline.application.tobpmn;

import com.protectline.application.tobpmn.blockstobpmn.FromBlockToBpmn;
import com.protectline.application.tobpmn.jstoblocks.FromJsProjectToBlocks;

import java.io.IOException;
import java.nio.file.Path;

public class JsProjectToBpmn {
    private final Path workingDirectory;
    private final FromJsProjectToBlocks jsToBlocks;
    private final FromBlockToBpmn blocksToBpmn;

    public JsProjectToBpmn(Path workingDirectory) {
        this.workingDirectory = workingDirectory;
        this.jsToBlocks = new FromJsProjectToBlocks(workingDirectory);
        this.blocksToBpmn = new FromBlockToBpmn(workingDirectory);
    }

    public void updateBpmn(String process) throws IOException {
        jsToBlocks.createBlocksFromJsProject();
        blocksToBpmn.updateBpmnFromBlocks(workingDirectory,process);
    }
}
