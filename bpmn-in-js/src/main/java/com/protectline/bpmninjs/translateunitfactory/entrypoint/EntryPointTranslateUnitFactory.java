package com.protectline.bpmninjs.translateunitfactory.entrypoint;

import com.protectline.bpmninjs.application.tojsproject.bpmntoblocks.MainBlockBuilder;
import com.protectline.bpmninjs.application.mainfactory.TranslateUnitAbstractFactory;
import com.protectline.bpmninjs.application.tobpmn.blockstobpmn.DocumentUpdater;
import com.protectline.bpmninjs.application.tobpmn.jstoblocks.UpdateBlockFromJs;
import com.protectline.bpmninjs.application.tojsproject.bpmntoblocks.BlockBuilder;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.common.block.BlockType;
import com.protectline.bpmninjs.jsproject.blocksfromelement.BlockFromElement;
import com.protectline.bpmninjs.translateunitfactory.template.TemplateUtil;
import com.protectline.bpmninjs.translateunitfactory.template.Template;

import java.util.List;
import java.util.Optional;

public class EntryPointTranslateUnitFactory implements TranslateUnitAbstractFactory {

    @Override
    public List<String> getElementNames() {
        return List.of("main");
    }

    @Override
    public Optional<BlockBuilder> createBlockBuilder() {
        return Optional.of(new MainBlockBuilder());
    }

    @Override
    public Optional<BlockFromElement> createBlockFromElement(Template template, String elementName) {
        if (canHandleElement(elementName)) {
            return Optional.of(new EntryPointBlockFromElement(template));
        }
        return Optional.empty();
    }

    @Override
    public Optional<UpdateBlockFromJs> createUpdateBlockFromJs(BlockType type) {
        // Main factory ne gère pas de type spécifique
        return Optional.empty();
    }

    @Override
    public Optional<BlockType> getBlockType() {
        // Main factory ne gère pas de type spécifique
        return Optional.empty();
    }

    @Override
    public Optional<DocumentUpdater> createBpmUpdater(Block block) {
        // Main factory ne gère pas les BpmUpdater pour l'instant
        return Optional.empty();
    }

    @Override
    public boolean canHandleElement(String elementName) {
        return "main".equals(elementName);
    }

    @Override
    public List<Template> getJsUpdaterTemplates(com.protectline.bpmninjs.files.FileUtil fileUtil) {
        try {
            return TemplateUtil
                    .getTemplate(fileUtil, "main");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load main templates", e);
        }
    }

    @Override
    public List<Template> getTemplatesForParser(com.protectline.bpmninjs.files.FileUtil fileUtil) {
        try {
            return TemplateUtil
                    .getTemplate(fileUtil, "main");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load main templates", e);
        }
    }

    @Override
    public Optional<com.protectline.bpmninjs.jsproject.JsUpdater> createJsUpdater(BlockType type, Template template) {
        // Entry point factory creates main updater regardless of BlockType
        return Optional.of(new EntryPointJsUpdater(template));
    }
}
