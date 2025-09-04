package com.protectline.bpmninjs.application.function;

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
import com.protectline.bpmninjs.model.template.Template;
import com.protectline.bpmninjs.model.template.TemplateUtil;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class FunctionTranslateUnitFactory implements TranslateUnitAbstractFactory {

    private static final Path TEMPLATE_FILE = getTemplateFile();
    
    private final Template template;
    private List<String> names;
    private BlockBuilder blockBuilder;
    private BlockFromElement blockFromElement;
    private UpdateBlock updateBlock;
    private BlockType blockType;
    private FunctionUpdater functionUpdater;

    public FunctionTranslateUnitFactory() {
        try {
            this.template = TemplateUtil.loadFromFile(TEMPLATE_FILE).get(0);
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

    private static Path getTemplateFile() {
        try {
            return Paths.get(FunctionTranslateUnitFactory.class.getClassLoader()
                .getResource("factorytemplates/functiontranslateunit/jsupdatertemplate.json").toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Failed to locate function template file", e);
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
    public Optional<BlockFromElement> createBlockFromElement() {
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
    public Optional<JsUpdater> createJsUpdater() {
        return Optional.of(functionUpdater);
    }
}
