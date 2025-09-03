package com.protectline.bpmninjs.application.mainfactory;

import com.protectline.bpmninjs.application.tobpmn.spi.DocumentUpdater;
import com.protectline.bpmninjs.application.tobpmn.spi.UpdateBlock;
import com.protectline.bpmninjs.application.tojsproject.spi.BlockBuilder;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.common.block.BlockType;
import com.protectline.bpmninjs.application.tobpmn.spi.BlockFromElement;
import com.protectline.bpmninjs.translateunitfactory.template.Template;

import java.util.List;
import java.util.Optional;

public interface TranslateUnitAbstractFactory {

    List<String> getElementNames();

    Optional<BlockBuilder> createBlockBuilder();

    Optional<BlockFromElement> createBlockFromElement(com.protectline.bpmninjs.files.FileUtil fileUtil, String elementName);

    Optional<UpdateBlock> createUpdateBlockFromJs(BlockType type);

    Optional<DocumentUpdater> createBpmUpdater(Block block);

    Optional<BlockType> getBlockType();

    Template getTemplate(com.protectline.bpmninjs.files.FileUtil fileUtil);

    Optional<com.protectline.bpmninjs.jsproject.JsUpdater> createJsUpdater(BlockType type, com.protectline.bpmninjs.files.FileUtil fileUtil);
}
