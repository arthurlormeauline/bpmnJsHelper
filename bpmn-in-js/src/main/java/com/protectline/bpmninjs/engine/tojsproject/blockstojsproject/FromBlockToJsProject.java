package com.protectline.bpmninjs.engine.tojsproject.blockstojsproject;

import com.protectline.bpmninjs.engine.mainfactory.MainFactory;
import com.protectline.bpmninjs.model.common.block.persist.BlockUtil;
import com.protectline.bpmninjs.model.common.block.Block;
import com.protectline.bpmninjs.engine.files.FileUtil;
import com.protectline.bpmninjs.model.jsproject.api.JsProject;
import com.protectline.bpmninjs.model.jsproject.JsProjectImpl;
import com.protectline.bpmninjs.engine.tojsproject.spi.JsUpdater;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static com.protectline.bpmninjs.model.common.block.persist.BlockUtil.readBlocksFromFile;

public class FromBlockToJsProject {

    private final FileUtil fileUtil;
    private final BlockUtil blockUtil;
    private final MainFactory mainFactory;

    public FromBlockToJsProject(FileUtil fileUtil, BlockUtil blockUtil, MainFactory mainFactory) throws IOException {
        this.fileUtil = fileUtil;
        this.blockUtil = blockUtil;
        this.mainFactory = mainFactory;
    }

    public void updateJsProjectFromBlocks(String process) throws IOException {
        List<Block> blocks = readBlocksFromFile(fileUtil.getBlocksFile(process));
        
        fileUtil.deleteJsDirectoryIfExists(process);
        copyTemplateToJsDirectory(process);
        
        JsProject jsProject = new JsProjectImpl(process, fileUtil, mainFactory);
        
        String jsContent = jsProject.getJsContent();
        var updaters = mainFactory.getJsUpdaters(blocks);
        
        String updatedContent = jsContent;
        for (JsUpdater updater : updaters) {
            updatedContent = updater.update(updatedContent, blocks);
        }
        
        jsProject.writeJsContent(updatedContent);
    }
    
    private void copyTemplateToJsDirectory(String process) throws IOException {
        Path destination = fileUtil.getJsProjectDirectory(process);
        Files.createDirectories(destination);
        
        // Copy the 3 template files from resources
        String[] templateFiles = {"BpmnRunner.js", "package.json", "script.js"};
        
        for (String fileName : templateFiles) {
            try (InputStream inputStream = getClass().getResourceAsStream("/jstemplate/" + fileName)) {
                if (inputStream == null) {
                    throw new IOException("Template file not found: " + fileName);
                }
                Path targetFile = destination.resolve(fileName);
                Files.copy(inputStream, targetFile, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }
}
