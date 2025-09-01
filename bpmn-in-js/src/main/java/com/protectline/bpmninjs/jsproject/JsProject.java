package com.protectline.bpmninjs.jsproject;

import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.files.FileUtil;
import com.protectline.bpmninjs.jsproject.parser.JsProjectParser;

import java.io.IOException;
import java.util.List;

import static com.protectline.bpmninjs.jsproject.jsupdater.JsUpdaterFactory.getJsUpdaters;
import static com.protectline.bpmninjs.jsproject.updatertemplate.JsUpdaterTemplateUtil.readJsUpdaterTemplatesFromFile;

public class JsProject {

    private final FileUtil fileUtil;
    private final JsProjectUpdater jsProjectUpdater;

    public JsProject(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
        jsProjectUpdater = new JsProjectUpdater(fileUtil);
    }

    public void updateProject(String process, List<Block> blocks) throws IOException {
        fileUtil.deleteJsDirectoryIfExists(process);
        fileUtil.copyTemplateToJsDirectory(process);
        var jsUpdaterTemplates = readJsUpdaterTemplatesFromFile(fileUtil);
        var updaters = getJsUpdaters(blocks, jsUpdaterTemplates);
        jsProjectUpdater.updateProject(process, blocks, updaters);
    }

    public List<Block> getBlocks(String process) throws IOException {
        var parser = new JsProjectParser(fileUtil);
        var jsContent = fileUtil.getJsRunnerFileContent(process);
        return parser.parseJsToBlocks(jsContent);
    }
}
