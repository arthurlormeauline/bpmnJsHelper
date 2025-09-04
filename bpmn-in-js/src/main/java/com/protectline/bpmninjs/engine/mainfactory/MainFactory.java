package com.protectline.bpmninjs.engine.mainfactory;

import com.protectline.bpmninjs.engine.mainfactory.main.MainTranslateUnit;
import com.protectline.bpmninjs.engine.tobpmn.spi.BpmnDocumentUpdater;
import com.protectline.bpmninjs.engine.tobpmn.spi.UpdateBlock;
import com.protectline.bpmninjs.engine.tojsproject.spi.BlockFromBpmnNode;
import com.protectline.bpmninjs.engine.tojsproject.spi.JsUpdater;
import com.protectline.bpmninjs.model.block.Block;
import com.protectline.bpmninjs.model.block.BlockType;
import com.protectline.bpmninjs.engine.files.FileService;
import com.protectline.bpmninjs.engine.tobpmn.spi.BlockFromJsNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class MainFactory {

    private final FileService fileService;
    private final List<TranslateUnit> translateFactories;
    private final Map<String, TranslateUnit> factoryByElement;

    public MainFactory(FileService fileService, List<TranslateUnit> translateFactories) throws IOException {
        this.fileService = fileService;
        this.translateFactories = new ArrayList<>();
        this.factoryByElement = new HashMap<>();

        addTranslateFactory(new MainTranslateUnit());

        for (TranslateUnit factory : translateFactories) {
            addTranslateFactory(factory);
        }
    }

    public void addTranslateFactory(TranslateUnit factory) {
        translateFactories.add(factory);
        
        List<String> elementNames = factory.getElementNames();
        if (elementNames.isEmpty()) {
            throw new IllegalArgumentException("Factory must handle specific elements: " + factory.getClass().getSimpleName());
        }
        for (String elementName : elementNames) {
            factoryByElement.put(elementName, factory);
        }
    }

    public UpdateBlock blockUpdaterFromJsFactory(BlockType type) {
        return translateFactories.stream()
                .filter(factory -> factory.getBlockType().isPresent() && factory.getBlockType().get().equals(type))
                .findFirst()
                .flatMap(factory -> factory.createUpdateBlockFromJs(type))
                .orElseThrow(() -> new IllegalArgumentException("No factory found for block type: " + type));
    }

    public List<BlockFromBpmnNode> getBlockBuilders() {
        return translateFactories.stream()
                .map(TranslateUnit::createBlockBuilder)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public BlockFromJsNode getBlockBuilder(String element) {
        TranslateUnit factory = factoryByElement.get(element);
        if (factory == null) {
            throw new UnsupportedOperationException("No factory found to handle element: " + element);
        }
        
        Optional<BlockFromJsNode> builder = factory.createBlockFromElement();
        if (builder.isPresent()) {
            return builder.get();
        }
        
        throw new UnsupportedOperationException("Factory found but could not create BlockFromElement for element: " + element);
    }


    public BpmnDocumentUpdater getBpmnUpdater(Block block) {
        return translateFactories.stream()
                .filter(factory -> factory.getBlockType().isPresent() && factory.getBlockType().get().equals(block.getType()))
                .findFirst()
                .flatMap(factory -> factory.createBpmUpdater(block))
                .orElseThrow(() -> new IllegalArgumentException("No Bpmn document updater for this type of block: " + block.getType()));
    }


    public List<JsUpdater> getJsUpdaters(List<Block> blocks) {
        List<JsUpdater> updaters = new ArrayList<>();
        
        translateFactories.stream()
                .filter(factory -> factory.getElementNames().contains("main"))
                .findFirst()
                .ifPresent(factory -> {
                    factory.createJsUpdater().ifPresent(updaters::add);
                });
        
        var blockTypes = blocks.stream().map(Block::getType).distinct().toList();
        for (BlockType blockType : blockTypes) {
            translateFactories.stream()
                    .filter(factory -> factory.getBlockType().isPresent() && factory.getBlockType().get().equals(blockType))
                    .findFirst()
                    .ifPresent(factory -> {
                        factory.createJsUpdater().ifPresent(updaters::add);
                    });
        }
        
        return updaters;
    }
}
