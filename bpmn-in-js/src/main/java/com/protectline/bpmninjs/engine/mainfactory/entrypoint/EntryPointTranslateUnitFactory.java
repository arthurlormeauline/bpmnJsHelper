package com.protectline.bpmninjs.engine.mainfactory.entrypoint;

import com.protectline.bpmninjs.engine.tojsproject.bpmntoblocks.MainBlockBuilder;
import com.protectline.bpmninjs.engine.mainfactory.TranslateUnitAbstractFactory;
import com.protectline.bpmninjs.engine.tobpmn.spi.DocumentUpdater;
import com.protectline.bpmninjs.engine.tobpmn.spi.UpdateBlock;
import com.protectline.bpmninjs.engine.tojsproject.spi.BlockBuilder;
import com.protectline.bpmninjs.engine.tojsproject.spi.JsUpdater;
import com.protectline.bpmninjs.model.common.block.Block;
import com.protectline.bpmninjs.model.common.block.BlockType;
import com.protectline.bpmninjs.engine.tobpmn.spi.BlockFromElement;
import com.protectline.bpmninjs.engine.mainfactory.entrypoint.tobpmn.EntryPointBlockFromElement;
import com.protectline.bpmninjs.engine.mainfactory.entrypoint.tojsproject.EntryPointJsUpdater;
import com.protectline.bpmninjs.model.template.TemplateUtil;
import com.protectline.bpmninjs.model.template.Template;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class EntryPointTranslateUnitFactory implements TranslateUnitAbstractFactory {

    private final Template template;
    private final Path workingDirectory;
    private EntryPointJsUpdater jsUpdater;
    private EntryPointBlockFromElement blockFromElement;
    private List<String> elementNames;

    public EntryPointTranslateUnitFactory(Path workingDirectory) {
        try {
            this.workingDirectory = workingDirectory;
            Path templateFile = workingDirectory.resolve("jsupdatertemplates")
                .resolve("main").resolve("jsupdatertemplate.json");
            this.template = TemplateUtil.loadFromFile(templateFile).get(0);
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
    public Optional<BlockFromElement> createBlockFromElement() {
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
    public Optional<JsUpdater> createJsUpdater() {
        return Optional.of(jsUpdater);
    }
}
