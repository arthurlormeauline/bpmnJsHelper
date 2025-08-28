package com.protectline.application.tobpmn;

import com.protectline.application.tobpmn.blockstobpmn.FromBlockToBpmn;
import com.protectline.application.tobpmn.jstoblocks.FromJsProjectToBlocks;
import com.protectline.files.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class JsProjectToBpmn {
    private final FromJsProjectToBlocks jsToBlocks;
    private final FromBlockToBpmn blocksToBpmn;

    public JsProjectToBpmn(FileUtil fileUtil) {
        this.jsToBlocks = new FromJsProjectToBlocks(fileUtil);
        this.blocksToBpmn = new FromBlockToBpmn(fileUtil);
    }

    public void updateBpmn(String process) throws IOException {
        jsToBlocks.createBlocksFromJsProject();
        blocksToBpmn.updateBpmnFromBlocks(process);
    }
}
