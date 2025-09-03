package com.protectline.bpmninjs.application.mainfactory;

import com.protectline.bpmninjs.application.tobpmn.blockstobpmn.DocumentUpdater;
import com.protectline.bpmninjs.application.tobpmn.jstoblocks.UpdateBlockFromJs;
import com.protectline.bpmninjs.application.tojsproject.bpmntoblocks.BlockBuilder;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.common.block.BlockType;
import com.protectline.bpmninjs.application.tobpmn.jstoblocks.BlockFromElement;
import com.protectline.bpmninjs.translateunitfactory.template.Template;

import java.util.List;
import java.util.Optional;

public interface TranslateUnitAbstractFactory {

    List<String> getElementNames();

    Optional<BlockBuilder> createBlockBuilder();

    Optional<BlockFromElement> createBlockFromElement(Template template, String elementName);

    Optional<UpdateBlockFromJs> createUpdateBlockFromJs(BlockType type);

    Optional<DocumentUpdater> createBpmUpdater(Block block);

    Optional<BlockType> getBlockType();

    boolean canHandleElement(String elementName);

    List<Template> getTemplate(com.protectline.bpmninjs.files.FileUtil fileUtil);

    Optional<com.protectline.bpmninjs.jsproject.JsUpdater> createJsUpdater(BlockType type, Template template);
}
