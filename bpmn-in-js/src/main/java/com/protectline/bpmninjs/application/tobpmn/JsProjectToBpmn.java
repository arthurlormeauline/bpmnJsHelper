package com.protectline.bpmninjs.application.tobpmn;

import com.protectline.bpmninjs.application.mainfactory.MainFactory;
import com.protectline.bpmninjs.application.tobpmn.blockstobpmn.FromBlockToBpmn;
import com.protectline.bpmninjs.application.tobpmn.jstoblocks.FromJsProjectToBlocks;
import com.protectline.bpmninjs.application.files.FileUtil;

import java.io.IOException;

public class JsProjectToBpmn {
    private final FromJsProjectToBlocks jsToBlocks;
    private final FromBlockToBpmn blocksToBpmn;

    public JsProjectToBpmn(FileUtil fileUtil, MainFactory mainFactory) {
        this.jsToBlocks = new FromJsProjectToBlocks(fileUtil, mainFactory);
        this.blocksToBpmn = new FromBlockToBpmn(fileUtil, mainFactory);
    }

    public void updateBpmn(String process) throws IOException {
        jsToBlocks.updateBlockFromJsProject(process);
        blocksToBpmn.updateBpmnFromBlocks(process);
    }
}
