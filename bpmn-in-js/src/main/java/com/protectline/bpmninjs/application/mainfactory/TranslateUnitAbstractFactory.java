package com.protectline.bpmninjs.application.mainfactory;

import com.protectline.bpmninjs.application.files.FileUtil;
import com.protectline.bpmninjs.application.tobpmn.spi.DocumentUpdater;
import com.protectline.bpmninjs.application.tobpmn.spi.UpdateBlock;
import com.protectline.bpmninjs.application.tojsproject.spi.BlockBuilder;
import com.protectline.bpmninjs.application.tojsproject.spi.JsUpdater;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.common.block.BlockType;
import com.protectline.bpmninjs.application.tobpmn.spi.BlockFromElement;
import com.protectline.bpmninjs.translateunitfactory.template.Template;

import java.util.List;
import java.util.Optional;

public interface TranslateUnitAbstractFactory {

    List<String> getElementNames();

    Optional<BlockBuilder> createBlockBuilder();

    Optional<BlockFromElement> createBlockFromElement(FileUtil fileUtil, String elementName);

    Optional<UpdateBlock> createUpdateBlockFromJs(BlockType type);

    Optional<DocumentUpdater> createBpmUpdater(Block block);

    Optional<BlockType> getBlockType();

    Template getTemplate(FileUtil fileUtil);

    Optional<JsUpdater> createJsUpdater(BlockType type, FileUtil fileUtil);
}
