package com.protectline.bpmninjs.jsproject;

import com.protectline.bpmninjs.application.mainfactory.MainFactory;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.files.FileUtil;
import com.protectline.bpmninjs.jsproject.blocksfromelement.JsProjectBlocksBuilder;

import java.io.IOException;
import java.util.List;

public class JsProject {

    private final FileUtil fileUtil;
    private final JsProjectUpdater jsProjectUpdater;
    private final UpdaterProvider updaterProvider;
    private final MainFactory mainFactory;

    public JsProject(FileUtil fileUtil, MainFactory mainFactory) throws IOException {
        this.fileUtil = fileUtil;
        jsProjectUpdater = new JsProjectUpdater(fileUtil);
        this.updaterProvider = mainFactory.getTemplateProvider();
        this.mainFactory = mainFactory;

    }

    public void updateProject(String process, List<Block> blocks) throws IOException {
        fileUtil.deleteJsDirectoryIfExists(process);
        fileUtil.copyTemplateToJsDirectory(process);
        var updaters = updaterProvider.getJsUpdaters(blocks);
        jsProjectUpdater.updateProject(process, blocks, updaters);
    }

    public List<Block> getBlocks(String process) throws IOException {
        var parser = new JsProjectBlocksBuilder(mainFactory);
        var jsContent = fileUtil.getJsRunnerFileContent(process);
        return parser.parseJsToBlocks(jsContent);
    }
}
