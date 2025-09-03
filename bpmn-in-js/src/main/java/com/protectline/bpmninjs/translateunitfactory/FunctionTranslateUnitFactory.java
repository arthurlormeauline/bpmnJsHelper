package com.protectline.bpmninjs.translateunitfactory;

import com.protectline.bpmninjs.application.tobpmn.blockstobpmn.BpmUpdater;
import com.protectline.bpmninjs.application.mainfactory.TranslateUnitAbstractFactory;
import com.protectline.bpmninjs.application.tobpmn.jstoblocks.UpdateBlockFromJs;
import com.protectline.bpmninjs.application.tojsproject.bpmntoblocks.BlockBuilder;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.common.block.BlockType;
import com.protectline.bpmninjs.jsproject.blocksfactory.BlockFromElement;
import com.protectline.bpmninjs.translateunit.TemplateForParser;
import com.protectline.bpmninjs.translateunit.JsUpdaterTemplate;

import java.util.List;
import java.util.Optional;

public class FunctionTranslateUnitFactory implements TranslateUnitAbstractFactory {

    @Override
    public List<String> getElementNames() {
        return List.of("function");
    }

    @Override
    public Optional<BlockBuilder> createBlockBuilder() {
        return Optional.of(new FunctionBlockBuilder());
    }

    @Override
    public Optional<BlockFromElement> createBlockFromElement(TemplateForParser template, String elementName) {
        if (canHandleElement(elementName)) {
            return Optional.of(new FunctionBlockFromElementBuilder(template));
        }
        return Optional.empty();
    }

    @Override
    public Optional<UpdateBlockFromJs> createUpdateBlockFromJs(BlockType type) {
        if (getBlockType().isPresent() && getBlockType().get().equals(type)) {
            return Optional.of(new FunctionBlockUpdaterFromJs());
        }
        return Optional.empty();
    }

    @Override
    public Optional<BlockType> getBlockType() {
        return Optional.of(BlockType.FUNCTION);
    }

    @Override
    public Optional<BpmUpdater> createBpmUpdater(Block block) {
        if (getBlockType().isPresent() && getBlockType().get().equals(block.getType())) {
            return Optional.of(new FunctionBpmnDocumentUpdater(block));
        }
        return Optional.empty();
    }

    @Override
    public boolean canHandleElement(String elementName) {
        return "function".equals(elementName);
    }

    @Override
    public List<JsUpdaterTemplate> getJsUpdaterTemplates(com.protectline.bpmninjs.files.FileUtil fileUtil) {
        try {
            return com.protectline.bpmninjs.translateunit.JsUpdaterTemplateUtil
                    .readJsUpdaterTemplatesFromFile(fileUtil, "functiontranslateunit");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load function templates", e);
        }
    }

    @Override
    public List<TemplateForParser> getTemplatesForParser(com.protectline.bpmninjs.files.FileUtil fileUtil) {
        try {
            return com.protectline.bpmninjs.translateunit.JsUpdaterTemplateUtil
                    .readTemplatesForParserFromFile(fileUtil, "functiontranslateunit");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load function templates", e);
        }
    }

    @Override
    public Optional<com.protectline.bpmninjs.jsproject.JsUpdater> createJsUpdater(BlockType type, JsUpdaterTemplate template) {
        if (getBlockType().isPresent() && getBlockType().get().equals(type)) {
            return Optional.of(new FunctionUpdater(template));
        }
        return Optional.empty();
    }
}
