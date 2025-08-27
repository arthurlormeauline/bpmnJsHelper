package com.protectline.application.tojsproject.blockstojsproject;

import com.protectline.common.block.Block;
import com.protectline.jsproject.JsProject;

import java.nio.file.Path;
import java.util.List;

public class FromBlockToJsProject {

    private final JsProject jsProject;

    public FromBlockToJsProject(Path workingDirectory) {
        jsProject = new JsProject(workingDirectory);
    }

    public void updateJsProjectFromBlocks(String process) {
        // get blocks from file
        List<Block> blocks = null;
        jsProject.updateProject(process, blocks);
    }
}
