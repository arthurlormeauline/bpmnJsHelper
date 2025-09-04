package com.protectline.bpmninjs.application.function;

import com.protectline.bpmninjs.engine.mainfactory.TranslateUnit;
import com.protectline.bpmninjs.engine.tobpmn.spi.BlockFromJsNode;
import com.protectline.bpmninjs.engine.tobpmn.spi.BpmnDocumentUpdater;
import com.protectline.bpmninjs.engine.tobpmn.spi.UpdateBlock;
import com.protectline.bpmninjs.engine.tojsproject.spi.BlockFromBpmnNode;
import com.protectline.bpmninjs.engine.tojsproject.spi.JsUpdater;
import com.protectline.bpmninjs.model.block.Block;
import com.protectline.bpmninjs.model.block.BlockType;
import com.protectline.bpmninjs.application.function.tobpmn.bpmndocumentUpdater.FromFuntionBlockFactory;
import com.protectline.bpmninjs.application.function.tobpmn.blockfromjsnode.FunctionBlockFromJsNode;
import com.protectline.bpmninjs.application.function.tobpmn.updateblock.UpdateFunctionBlocks;
import com.protectline.bpmninjs.application.function.tojsproject.blockfrombpmnnode.FunctionBlockFromBpmnNode;
import com.protectline.bpmninjs.application.function.tojsproject.jsupdater.FunctionJsUpdater;
import com.protectline.bpmninjs.model.template.Template;
import com.protectline.bpmninjs.model.template.TemplateUtil;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class FunctionTranslateUnit implements TranslateUnit {

    private static final Path TEMPLATE_FILE = getTemplateFile();
    
    private final Template template;
    private List<String> names;
    private BlockFromBpmnNode blockFromBpmnNode;
    private BlockFromJsNode blockFromJsNode;
    private UpdateBlock updateBlock;
    private BlockType blockType;
    private FunctionJsUpdater functionJsUpdater;

    public FunctionTranslateUnit() {
        try {
            this.template = TemplateUtil.loadFromFile(TEMPLATE_FILE).get(0);
            this.blockFromBpmnNode = new FunctionBlockFromBpmnNode();
            this.names = List.of("function");
            this.blockType = BlockType.FUNCTION;
            this.blockFromJsNode = new FunctionBlockFromJsNode(template);
            this.updateBlock = new UpdateFunctionBlocks();
            this.functionJsUpdater = new FunctionJsUpdater(template);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load function templates", e);
        }
    }

    private static Path getTemplateFile() {
        try {
            return Paths.get(FunctionTranslateUnit.class.getClassLoader()
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
    public Optional<BlockFromBpmnNode> createBlockBuilder() {
        return Optional.of(blockFromBpmnNode);
    }

    @Override
    public Optional<BlockFromJsNode> createBlockFromElement() {
        return Optional.of(blockFromJsNode);
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
    public Optional<BpmnDocumentUpdater> createBpmUpdater(Block block) {
        return Optional.of(FromFuntionBlockFactory.getFromBlock(block));
    }

    @Override
    public Optional<JsUpdater> createJsUpdater() {
        return Optional.of(functionJsUpdater);
    }
}
