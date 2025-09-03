package com.protectline.bpmninjs.translateunitfactory.entrypoint;

import com.protectline.bpmninjs.application.files.FileUtil;
import com.protectline.bpmninjs.application.tojsproject.bpmntoblocks.MainBlockBuilder;
import com.protectline.bpmninjs.application.mainfactory.TranslateUnitAbstractFactory;
import com.protectline.bpmninjs.application.tobpmn.spi.DocumentUpdater;
import com.protectline.bpmninjs.application.tobpmn.spi.UpdateBlock;
import com.protectline.bpmninjs.application.tojsproject.spi.BlockBuilder;
import com.protectline.bpmninjs.application.tojsproject.spi.JsUpdater;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.common.block.BlockType;
import com.protectline.bpmninjs.application.tobpmn.spi.BlockFromElement;
import com.protectline.bpmninjs.translateunitfactory.entrypoint.tobpmn.EntryPointBlockFromElement;
import com.protectline.bpmninjs.translateunitfactory.entrypoint.tojsproject.EntryPointJsUpdater;
import com.protectline.bpmninjs.translateunitfactory.template.persist.TemplateUtil;
import com.protectline.bpmninjs.translateunitfactory.template.Template;

import java.util.List;
import java.util.Optional;

public class EntryPointTranslateUnitFactory implements TranslateUnitAbstractFactory {

    private final Template template;
    private EntryPointJsUpdater jsUpdater;
    private EntryPointBlockFromElement blockFromElement;
    private List<String> elementNames;

    public EntryPointTranslateUnitFactory(FileUtil fileUtil) {
        try {
            this.template = TemplateUtil.getTemplate(fileUtil, "main").get(0);
            this.blockFromElement = new EntryPointBlockFromElement(template);
            this.jsUpdater = new EntryPointJsUpdater(template);
            this.elementNames = List.of("main");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load main templates", e);
        }
    }

    @Override
    public List<String> getElementNames() {
        return elementNames;
    }

    @Override
    public Optional<BlockBuilder> createBlockBuilder() {
        return Optional.of(new MainBlockBuilder());
    }

    @Override
    public Optional<BlockFromElement> createBlockFromElement(FileUtil fileUtil, String elementName) {
        return Optional.of(blockFromElement);
    }

    @Override
    public Optional<UpdateBlock> createUpdateBlockFromJs(BlockType type) {
        return Optional.empty();
    }

    @Override
    public Optional<BlockType> getBlockType() {
        return Optional.empty();
    }

    @Override
    public Optional<DocumentUpdater> createBpmUpdater(Block block) {
        return Optional.empty();
    }

    @Override
    public Template getTemplate(FileUtil fileUtil) {
        return template;
    }

    @Override
    public Optional<JsUpdater> createJsUpdater(BlockType type, FileUtil fileUtil) {
        return Optional.of(jsUpdater);
    }
}
