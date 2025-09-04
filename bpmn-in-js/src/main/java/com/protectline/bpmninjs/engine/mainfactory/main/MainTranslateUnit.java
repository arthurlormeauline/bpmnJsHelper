package com.protectline.bpmninjs.engine.mainfactory.main;

import com.protectline.bpmninjs.engine.tojsproject.bpmntoblocks.MainBlockFromBpmnNode;
import com.protectline.bpmninjs.engine.mainfactory.TranslateUnit;
import com.protectline.bpmninjs.engine.tobpmn.spi.BpmnDocumentUpdater;
import com.protectline.bpmninjs.engine.tobpmn.spi.UpdateBlock;
import com.protectline.bpmninjs.engine.tojsproject.spi.BlockFromBpmnNode;
import com.protectline.bpmninjs.engine.tojsproject.spi.JsUpdater;
import com.protectline.bpmninjs.model.block.Block;
import com.protectline.bpmninjs.model.block.BlockType;
import com.protectline.bpmninjs.engine.tobpmn.spi.BlockFromJsNode;
import com.protectline.bpmninjs.engine.mainfactory.main.tobpmn.MainBlockFromJsNode;
import com.protectline.bpmninjs.engine.mainfactory.main.tojsproject.MainJsUpdater;
import com.protectline.bpmninjs.model.template.TemplateUtil;
import com.protectline.bpmninjs.model.template.Template;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class MainTranslateUnit implements TranslateUnit {

    private static final Path TEMPLATE_FILE = getTemplateFile();
    
    private final Template template;
    private MainJsUpdater jsUpdater;
    private MainBlockFromJsNode blockFromElement;
    private List<String> elementNames;

    public MainTranslateUnit() {
        try {
            this.template = TemplateUtil.loadFromFile(TEMPLATE_FILE).get(0);
            this.blockFromElement = new MainBlockFromJsNode(template);
            this.jsUpdater = new MainJsUpdater(template);
            this.elementNames = List.of("main");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load entrypoint templates", e);
        }
    }
    
    private static Path getTemplateFile() {
        try {
            return Paths.get(MainTranslateUnit.class.getClassLoader()
                .getResource("factorytemplates/entrypoint/jsupdatertemplate.json").toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Failed to locate entrypoint template file", e);
        }
    }

    @Override
    public List<String> getElementNames() {
        return elementNames;
    }

    @Override
    public Optional<BlockFromBpmnNode> createBlockBuilder() {
        return Optional.of(new MainBlockFromBpmnNode());
    }

    @Override
    public Optional<BlockFromJsNode> createBlockFromElement() {
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
    public Optional<BpmnDocumentUpdater> createBpmUpdater(Block block) {
        return Optional.empty();
    }

    @Override
    public Optional<JsUpdater> createJsUpdater() {
        return Optional.of(jsUpdater);
    }
}
