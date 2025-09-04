package com.protectline.bpmninjs.engine.tobpmn;

import com.protectline.bpmninjs.engine.mainfactory.MainFactory;
import com.protectline.bpmninjs.engine.tobpmn.blockstobpmn.FromBlockToBpmn;
import com.protectline.bpmninjs.engine.tobpmn.jstoblocks.FromJsProjectToBlocks;
import com.protectline.bpmninjs.engine.files.FileService;

import java.io.IOException;

public class JsProjectToBpmn {
    private final FromJsProjectToBlocks jsToBlocks;
    private final FromBlockToBpmn blocksToBpmn;

    public JsProjectToBpmn(FileService fileService, MainFactory mainFactory) {
        this.jsToBlocks = new FromJsProjectToBlocks(fileService, mainFactory);
        this.blocksToBpmn = new FromBlockToBpmn(fileService, mainFactory);
    }

    public void updateBpmn(String process) throws IOException {
        jsToBlocks.updateBlockFromJsProject(process);
        blocksToBpmn.updateBpmnFromBlocks(process);
    }
}
