package com.protectline.jsproject;

import com.protectline.common.block.Block;
import com.protectline.files.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class JsProjectUpdater {

    private final FileUtil fileUtil;
    private List<JsUpdater> updaters;

    public JsProjectUpdater(FileUtil fileUtil) {
        this.fileUtil=fileUtil;
    }

    public void updateProject(String process, List<Block> blocks, List<JsUpdater> updaters) throws IOException {
        Path jsProjectDirectory = fileUtil.getJsProjectDirectory(process);
        Path bpmnRunnerFile = jsProjectDirectory.resolve("BpmnRunner.js");
        String jsContent = Files.readString(bpmnRunnerFile);
        
        String updatedContent = jsContent;
        for (JsUpdater updater : updaters) {
            updatedContent = updater.update(updatedContent, blocks);
        }
        
        Files.writeString(bpmnRunnerFile, updatedContent);
    }
}
