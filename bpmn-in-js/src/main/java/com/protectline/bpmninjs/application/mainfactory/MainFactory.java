package com.protectline.bpmninjs.application.mainfactory;

import com.protectline.bpmninjs.application.tobpmn.blockstobpmn.DocumentUpdater;
import com.protectline.bpmninjs.application.tobpmn.jstoblocks.UpdateBlockFromJs;
import com.protectline.bpmninjs.application.tojsproject.bpmntoblocks.BlockBuilder;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.common.block.BlockType;
import com.protectline.bpmninjs.files.FileUtil;
import com.protectline.bpmninjs.jsproject.blocksfromelement.BlockFromElement;
import com.protectline.bpmninjs.translateunitfactory.entrypoint.EntryPointJsUpdater;
import com.protectline.bpmninjs.translateunitfactory.template.Template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class MainFactory {

    private final List<Template> jsTemplateUpdaters;
    private final List<Template> templatesForParser;
    private final FileUtil fileUtil;
    private final List<TranslateUnitAbstractFactory> translateFactories;
    private final Map<String, TranslateUnitAbstractFactory> factoryByElement;
    private TranslateUnitAbstractFactory fallbackFactory;

    public MainFactory(FileUtil fileUtil, TranslateUnitFactoryProvider factoryProvider) throws IOException {
        this.fileUtil = fileUtil;
        this.translateFactories = new ArrayList<>();
        this.factoryByElement = new HashMap<>();
        this.jsTemplateUpdaters = new ArrayList<>();
        this.templatesForParser = new ArrayList<>();
        
        // Load all factories from provider
        for (TranslateUnitAbstractFactory factory : factoryProvider.getTranslateUnitFactories()) {
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
        
        // Collecter les templates de cette factory
        try {
            jsTemplateUpdaters.addAll(factory.getJsUpdaterTemplates(fileUtil));
            templatesForParser.addAll(factory.getTemplatesForParser(fileUtil));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load templates from factory: " + factory.getClass().getSimpleName(), e);
        }
    }


    public UpdateBlockFromJs blockUpdaterFromJsFactory(BlockType type) {
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

    /**
     * Remplace la logique de BlockFromElementFactory#getBlockBuilder
     * Sélectionne la bonne factory basée sur l'élément et crée le BlockFromElement approprié
     */
    public BlockFromElement getBlockBuilder(String element) {
        Template template = getTemplateByElement(element);
        
        TranslateUnitAbstractFactory factory = factoryByElement.get(element);
        if (factory == null) {
            throw new UnsupportedOperationException("No factory found to handle element: " + element);
        }
        
        Optional<BlockFromElement> builder = factory.createBlockFromElement(template, element);
        if (builder.isPresent()) {
            return builder.get();
        }
        
        throw new UnsupportedOperationException("Factory found but could not create BlockFromElement for element: " + element);
    }

    /**
     * Remplace la logique de BpmnDocumentUpdaterFactory#getupdater
     * Sélectionne la bonne factory basée sur le block et crée le BpmUpdater approprié
     */
    public DocumentUpdater getBpmnUpdater(Block block) {
        return translateFactories.stream()
                .filter(factory -> factory.getBlockType().isPresent() && factory.getBlockType().get().equals(block.getType()))
                .findFirst()
                .flatMap(factory -> factory.createBpmUpdater(block))
                .orElseThrow(() -> new IllegalArgumentException("No Bpmn document updater for this type of block: " + block.getType()));
    }

    /**
     * Remplace la logique de JsUpdaterFactory#getJsUpdaters
     * Crée les JsUpdater en utilisant les factories et templates
     */
    public List<com.protectline.bpmninjs.jsproject.JsUpdater> getJsUpdaters(List<Block> blocks) {
        List<com.protectline.bpmninjs.jsproject.JsUpdater> updaters = new ArrayList<>();
        
        // Ajouter le main updater (EntryPoint)
        Template mainTemplate = getJsUpdaterTemplate("MAIN");
        updaters.add(new EntryPointJsUpdater(mainTemplate));
        
        // Ajouter les updaters basés sur les blocks
        var blockTypes = blocks.stream().map(Block::getType).distinct().toList();
        for (BlockType blockType : blockTypes) {
            translateFactories.stream()
                    .filter(factory -> factory.getBlockType().isPresent() && factory.getBlockType().get().equals(blockType))
                    .findFirst()
                    .ifPresent(factory -> {
                        Template template = getJsUpdaterTemplate(getTemplateName(blockType));
                        factory.createJsUpdater(blockType, template).ifPresent(updaters::add);
                    });
        }
        
        return updaters;
    }

    private Template getJsUpdaterTemplate(String templateName) {
        return jsTemplateUpdaters.stream()
                .filter(template -> template.getName().equals(templateName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No template found for: " + templateName));
    }

    private String getTemplateName(BlockType blockType) {
        return blockType.name(); // FUNCTION -> "FUNCTION"
    }
    
    private Template getTemplateByElement(String elementName) {
        return templatesForParser.stream()
            .filter(template -> template.getElement().equals(elementName))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No template found for element: " + elementName));
    }
    
}
