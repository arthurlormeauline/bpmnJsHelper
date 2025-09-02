package com.protectline.bpmninjs.application;

import com.protectline.bpmninjs.application.tobpmn.jstoblocks.BlockUpdaterFromJs;
import com.protectline.bpmninjs.application.tobpmn.jstoblocks.UpdateBlockFromJs;
import com.protectline.bpmninjs.common.block.BlockType;
import com.protectline.bpmninjs.files.FileUtil;
import com.protectline.bpmninjs.jsproject.TemplateProvider;
import com.protectline.bpmninjs.jsproject.blocksfactory.blockbuilder.BlockFromElementFactory;
import com.protectline.bpmninjs.jsproject.updatertemplate.JsUpdaterTemplate;

import java.io.IOException;
import java.util.List;

import static com.protectline.bpmninjs.jsproject.updatertemplate.JsUpdaterTemplateUtil.readJsUpdaterTemplatesFromFile;

public class MainProvider {

    private final List<JsUpdaterTemplate> jsTemplateUpdaters;
    private final FileUtil fileUtil;

    MainProvider(FileUtil fileUtil) throws IOException {
        this.jsTemplateUpdaters = readJsUpdaterTemplatesFromFile(fileUtil);
        this.fileUtil = fileUtil;
    }

    public BuildersProvider getBuilderProvider() {
        return new BuildersProvider();
    }

    public BlockFileUtilProvider getBlockFileUtilProvider() {
        return new FunctionBlockFileUtilProvider();
    }

    public TemplateProvider getTemplateProvider() {
        return new TemplateProvider(jsTemplateUpdaters);
    }

    public BlockFromElementFactory getBlockFromElementFactory() throws IOException {
        return new BlockFromElementFactory(fileUtil);
    }

    public UpdateBlockFromJs jsUpdaterFomBlockFactory(BlockType type) {
        switch (type) {
            case FUNCTION:
                return new BlockUpdaterFromJs();
            default:
                throw new IllegalArgumentException("Unsupported block type: " + type);
        }
    }
}
