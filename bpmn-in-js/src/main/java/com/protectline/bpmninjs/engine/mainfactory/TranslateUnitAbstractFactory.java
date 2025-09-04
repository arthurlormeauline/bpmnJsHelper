package com.protectline.bpmninjs.engine.mainfactory;

import com.protectline.bpmninjs.engine.files.FileUtil;
import com.protectline.bpmninjs.engine.tobpmn.spi.DocumentUpdater;
import com.protectline.bpmninjs.engine.tobpmn.spi.UpdateBlock;
import com.protectline.bpmninjs.engine.tojsproject.spi.BlockBuilder;
import com.protectline.bpmninjs.engine.tojsproject.spi.JsUpdater;
import com.protectline.bpmninjs.model.common.block.Block;
import com.protectline.bpmninjs.model.common.block.BlockType;
import com.protectline.bpmninjs.engine.tobpmn.spi.BlockFromElement;
import com.protectline.bpmninjs.application.template.Template;

import java.util.List;
import java.util.Optional;

public interface TranslateUnitAbstractFactory {

    List<String> getElementNames();

    Optional<BlockBuilder> createBlockBuilder();

    Optional<BlockFromElement> createBlockFromElement(FileUtil fileUtil, String elementName);

    Optional<UpdateBlock> createUpdateBlockFromJs(BlockType type);

    Optional<DocumentUpdater> createBpmUpdater(Block block);

    Optional<BlockType> getBlockType();

    Optional<JsUpdater> createJsUpdater(BlockType type, FileUtil fileUtil);
}
