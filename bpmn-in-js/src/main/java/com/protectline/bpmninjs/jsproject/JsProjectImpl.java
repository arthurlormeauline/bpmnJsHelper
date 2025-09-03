package com.protectline.bpmninjs.jsproject;

import com.protectline.bpmninjs.application.mainfactory.MainFactory;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.files.FileUtil;
import com.protectline.bpmninjs.jsproject.blocksfromelement.JsProjectBlocksBuilder;

import java.io.IOException;
import java.util.List;

public class JsProjectImpl implements JsProject {

    private final FileUtil fileUtil;
    private final MainFactory mainFactory;
    private final String process;

    public JsProjectImpl(String process, FileUtil fileUtil, MainFactory mainFactory) {
        this.process = process;
        this.fileUtil = fileUtil;
        this.mainFactory = mainFactory;
    }

    @Override
    public List<Block> getBlocks() throws IOException {
        var parser = new JsProjectBlocksBuilder(mainFactory);
        var jsContent = fileUtil.getJsRunnerFileContent(process);
        return parser.parseJsToBlocks(jsContent);
    }

    @Override
    public String getJsContent() throws IOException {
        return fileUtil.getJsRunnerFileContent(process);
    }

    @Override
    public void writeJsContent(String updatedContent) throws IOException {
        java.nio.file.Path jsProjectDirectory = fileUtil.getJsProjectDirectory(process);
        java.nio.file.Path bpmnRunnerFile = jsProjectDirectory.resolve("BpmnRunner.js");
        java.nio.file.Files.writeString(bpmnRunnerFile, updatedContent);
    }
}