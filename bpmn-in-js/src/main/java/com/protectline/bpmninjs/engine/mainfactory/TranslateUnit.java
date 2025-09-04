package com.protectline.bpmninjs.engine.mainfactory;

import com.protectline.bpmninjs.engine.tobpmn.spi.BpmnDocumentUpdater;
import com.protectline.bpmninjs.engine.tobpmn.spi.UpdateBlock;
import com.protectline.bpmninjs.engine.tojsproject.spi.BlockFromBpmnNode;
import com.protectline.bpmninjs.engine.tojsproject.spi.JsUpdater;
import com.protectline.bpmninjs.model.block.Block;
import com.protectline.bpmninjs.model.block.BlockType;
import com.protectline.bpmninjs.engine.tobpmn.spi.BlockFromJsNode;

import java.util.List;
import java.util.Optional;

public interface TranslateUnit {

    List<String> getElementNames();

    Optional<BlockFromBpmnNode> createBlockBuilder();

    Optional<BlockFromJsNode> createBlockFromElement();

    Optional<UpdateBlock> createUpdateBlockFromJs(BlockType type);

    Optional<BpmnDocumentUpdater> createBpmUpdater(Block block);

    Optional<BlockType> getBlockType();

    Optional<JsUpdater> createJsUpdater();
}
