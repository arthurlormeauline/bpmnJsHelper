package com.protectline.bpmninjs.jsproject;

import com.protectline.bpmninjs.application.MainProvider;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.files.FileUtil;
import com.protectline.bpmninjs.jsproject.blocksfactory.JsProjectBlocksBuilder;
import com.protectline.bpmninjs.jsproject.blocksfactory.blockbuilder.BlockFromElementFactory;

import java.io.IOException;
import java.util.List;

public class JsProject {

    private final FileUtil fileUtil;
    private final JsProjectUpdater jsProjectUpdater;
    private final TemplateProvider templateProvider;
    private final BlockFromElementFactory blockFromElementFactory;

    public JsProject(FileUtil fileUtil, MainProvider mainProvider) {
        this.fileUtil = fileUtil;
        jsProjectUpdater = new JsProjectUpdater(fileUtil);
        this.templateProvider= mainProvider.getTemplateProvider();
        this.blockFromElementFactory = mainProvider.getBlockFromElementFactory();

    }

    public void updateProject(String process, List<Block> blocks) throws IOException {
        fileUtil.deleteJsDirectoryIfExists(process);
        fileUtil.copyTemplateToJsDirectory(process);
        var updaters = templateProvider.getJsUpdaters(blocks);
        jsProjectUpdater.updateProject(process, blocks, updaters);
    }

    public List<Block> getBlocks(String process) throws IOException {
        var parser = new JsProjectBlocksBuilder(blockFromElementFactory);
        var jsContent = fileUtil.getJsRunnerFileContent(process);
        return parser.parseJsToBlocks(jsContent);
    }
}
