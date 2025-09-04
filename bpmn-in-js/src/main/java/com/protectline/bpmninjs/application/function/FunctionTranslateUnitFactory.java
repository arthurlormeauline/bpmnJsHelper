package com.protectline.bpmninjs.application.function;

import com.protectline.bpmninjs.engine.files.FileUtil;
import com.protectline.bpmninjs.engine.mainfactory.TranslateUnitAbstractFactory;
import com.protectline.bpmninjs.engine.tobpmn.spi.BlockFromElement;
import com.protectline.bpmninjs.engine.tobpmn.spi.DocumentUpdater;
import com.protectline.bpmninjs.engine.tobpmn.spi.UpdateBlock;
import com.protectline.bpmninjs.engine.tojsproject.spi.BlockBuilder;
import com.protectline.bpmninjs.engine.tojsproject.spi.JsUpdater;
import com.protectline.bpmninjs.model.common.block.Block;
import com.protectline.bpmninjs.model.common.block.BlockType;
import com.protectline.bpmninjs.application.function.tobpmn.fromblocktobpmn.documentUpdater.FromFuntionBlockFactory;
import com.protectline.bpmninjs.application.function.tobpmn.fromjsprojecttoblock.blockfromelement.FunctionBlockFromElement;
import com.protectline.bpmninjs.application.function.tobpmn.fromjsprojecttoblock.updateblock.UpdateFunctionBlocks;
import com.protectline.bpmninjs.application.function.tojsproject.blockbuilder.FunctionBlockBuilder;
import com.protectline.bpmninjs.application.function.tojsproject.jsupdater.FunctionUpdater;
import com.protectline.bpmninjs.application.template.Template;
import com.protectline.bpmninjs.application.template.persist.TemplateUtil;

import java.util.List;
import java.util.Optional;

public class FunctionTranslateUnitFactory implements TranslateUnitAbstractFactory {

    private final Template template;
    private List<String> names;
    private BlockBuilder blockBuilder;
    private BlockFromElement blockFromElement;
    private UpdateBlock updateBlock;
    private BlockType blockType;
    private FunctionUpdater functionUpdater;

    public FunctionTranslateUnitFactory(FileUtil fileUtil) {
        try {
            this.template = TemplateUtil.getTemplate(fileUtil, "functiontranslateunit").get(0);
            this.blockBuilder = new FunctionBlockBuilder();
            this.names = List.of("function");
            this.blockType = BlockType.FUNCTION;
            this.blockFromElement = new FunctionBlockFromElement(template);
            this.updateBlock = new UpdateFunctionBlocks();
            this.functionUpdater = new FunctionUpdater(template);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load function templates", e);
        }
    }

    @Override
    public List<String> getElementNames() {
        return names;
    }

    @Override
    public Optional<BlockBuilder> createBlockBuilder() {
        return Optional.of(blockBuilder);
    }

    @Override
    public Optional<BlockFromElement> createBlockFromElement(FileUtil fileUtil, String elementName) {
        return Optional.of(blockFromElement);
    }

    @Override
    public Optional<UpdateBlock> createUpdateBlockFromJs(BlockType type) {
        return Optional.of(updateBlock);
    }

    @Override
    public Optional<BlockType> getBlockType() {
        return Optional.of(blockType);
    }

    @Override
    public Optional<DocumentUpdater> createBpmUpdater(Block block) {
        return Optional.of(FromFuntionBlockFactory.getFromBlock(block));
    }

    @Override
    public Optional<JsUpdater> createJsUpdater(BlockType type, FileUtil fileUtil) {
        return Optional.of(functionUpdater);
    }
}
