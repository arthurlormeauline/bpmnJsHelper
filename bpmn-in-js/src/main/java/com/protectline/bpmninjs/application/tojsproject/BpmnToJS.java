package com.protectline.bpmninjs.application.tojsproject;

import com.protectline.bpmninjs.application.BlockWriter;
import com.protectline.bpmninjs.application.MainProvider;
import com.protectline.bpmninjs.application.tojsproject.blockstojsproject.FromBlockToJsProject;
import com.protectline.bpmninjs.application.tojsproject.bpmntoblocks.FromBpmnToBlocks;
import com.protectline.bpmninjs.files.FileUtil;

import java.io.IOException;

public class BpmnToJS {

    private final FromBpmnToBlocks bpmnToBlock;
    private final FromBlockToJsProject blockToJs;

    public BpmnToJS(FileUtil fileUtil, MainProvider mainProvider) throws IOException {
        bpmnToBlock = new FromBpmnToBlocks(fileUtil, mainProvider.getBuilderProvider(), mainProvider.getBlockFileUtilProvider().getBlockWriterFactory());
        blockToJs = new FromBlockToJsProject(fileUtil, new BlockWriter(), mainProvider.getTemplateProvider());
    }

    public void createProject(String process) throws IOException {
        bpmnToBlock.createBlocksFromBpmn(process);
        blockToJs.updateJsProjectFromBlocks(process);
    }
}
