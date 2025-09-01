package com.protectline.bpmninjs.application.tobpmn;

import com.protectline.bpmninjs.application.tobpmn.blockstobpmn.FromBlockToBpmn;
import com.protectline.bpmninjs.application.tobpmn.jstoblocks.FromJsProjectToBlocks;
import com.protectline.bpmninjs.files.FileUtil;

import java.io.IOException;

public class JsProjectToBpmn {
    private final FromJsProjectToBlocks jsToBlocks;
    private final FromBlockToBpmn blocksToBpmn;

    public JsProjectToBpmn(FileUtil fileUtil) {
        this.jsToBlocks = new FromJsProjectToBlocks(fileUtil);
        this.blocksToBpmn = new FromBlockToBpmn(fileUtil);
    }

    public void updateBpmn(String process) throws IOException {
        jsToBlocks.updateBlockFromJsProject(process);
        blocksToBpmn.updateBpmnFromBlocks(process);
    }
}
