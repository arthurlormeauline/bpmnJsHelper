package com.protectline.bpmninjs.engine.mainfactory;

import com.protectline.bpmninjs.engine.mainfactory.entrypoint.EntryPointTranslateUnitFactory;
import com.protectline.bpmninjs.engine.tobpmn.spi.DocumentUpdater;
import com.protectline.bpmninjs.engine.tobpmn.spi.UpdateBlock;
import com.protectline.bpmninjs.engine.tojsproject.spi.BlockBuilder;
import com.protectline.bpmninjs.engine.tojsproject.spi.JsUpdater;
import com.protectline.bpmninjs.model.common.block.Block;
import com.protectline.bpmninjs.model.common.block.BlockType;
import com.protectline.bpmninjs.engine.files.FileUtil;
import com.protectline.bpmninjs.engine.tobpmn.spi.BlockFromElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class MainFactory {

    private final FileUtil fileUtil;
    private final List<TranslateUnitAbstractFactory> translateFactories;
    private final Map<String, TranslateUnitAbstractFactory> factoryByElement;

    public MainFactory(FileUtil fileUtil, List<TranslateUnitAbstractFactory> translateFactories) throws IOException {
        this.fileUtil = fileUtil;
        this.translateFactories = new ArrayList<>();
        this.factoryByElement = new HashMap<>();

        addTranslateFactory(new EntryPointTranslateUnitFactory(fileUtil.getWorkingDirectory()));

        for (TranslateUnitAbstractFactory factory : translateFactories) {
            addTranslateFactory(factory);
        }
    }

    public void addTranslateFactory(TranslateUnitAbstractFactory factory) {
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

    public List<BlockBuilder> getBlockBuilders() {
        return translateFactories.stream()
                .map(TranslateUnitAbstractFactory::createBlockBuilder)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public BlockFromElement getBlockBuilder(String element) {
        TranslateUnitAbstractFactory factory = factoryByElement.get(element);
        if (factory == null) {
            throw new UnsupportedOperationException("No factory found to handle element: " + element);
        }
        
        Optional<BlockFromElement> builder = factory.createBlockFromElement();
        if (builder.isPresent()) {
            return builder.get();
        }
        
        throw new UnsupportedOperationException("Factory found but could not create BlockFromElement for element: " + element);
    }


    public DocumentUpdater getBpmnUpdater(Block block) {
        return translateFactories.stream()
                .filter(factory -> factory.getBlockType().isPresent() && factory.getBlockType().get().equals(block.getType()))
                .findFirst()
                .flatMap(factory -> factory.createBpmUpdater(block))
                .orElseThrow(() -> new IllegalArgumentException("No Bpmn document updater for this type of block: " + block.getType()));
    }


    public List<JsUpdater> getJsUpdaters(List<Block> blocks) {
        List<JsUpdater> updaters = new ArrayList<>();
        
        // Ajouter l'EntryPoint updater
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
