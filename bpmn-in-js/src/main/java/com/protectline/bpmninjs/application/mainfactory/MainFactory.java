package com.protectline.bpmninjs.application.mainfactory;

import com.protectline.bpmninjs.application.tobpmn.blockstobpmn.BpmUpdater;
import com.protectline.bpmninjs.application.tobpmn.jstoblocks.UpdateBlockFromJs;
import com.protectline.bpmninjs.application.tojsproject.bpmntoblocks.BlockBuilder;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.common.block.BlockType;
import com.protectline.bpmninjs.files.FileUtil;
import com.protectline.bpmninjs.jsproject.UpdaterProvider;
import com.protectline.bpmninjs.jsproject.blocksfactory.BlockFromElement;
import com.protectline.bpmninjs.jsproject.updatertemplate.JsUpdaterTemplate;
import com.protectline.bpmninjs.jsproject.updatertemplate.JsUpdaterTemplateUtil;
import com.protectline.bpmninjs.jsproject.updatertemplate.TemplateForParser;
import com.protectline.bpmninjs.application.entrypointfactory.EntryPointTranslateUnitFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.protectline.bpmninjs.jsproject.updatertemplate.JsUpdaterTemplateUtil.readJsUpdaterTemplatesFromFile;

public class MainFactory {

    private final List<JsUpdaterTemplate> jsTemplateUpdaters;
    private final List<TemplateForParser> templatesForParser;
    private final FileUtil fileUtil;
    private final List<TranslateUnitAbstractFactory> translateFactories;
    private final Map<String, TranslateUnitAbstractFactory> factoryByElement;
    private TranslateUnitAbstractFactory fallbackFactory;

    public MainFactory(FileUtil fileUtil) throws IOException {
        this.jsTemplateUpdaters = readJsUpdaterTemplatesFromFile(fileUtil);
        this.templatesForParser = JsUpdaterTemplateUtil.readTemplatesForParserFromFile(fileUtil);
        this.fileUtil = fileUtil;
        this.translateFactories = new ArrayList<>();
        this.factoryByElement = new HashMap<>();
        addTranslateFactory(new EntryPointTranslateUnitFactory());
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

    public UpdaterProvider getTemplateProvider() {
        return new UpdaterProvider(jsTemplateUpdaters);
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
        TemplateForParser template = getTemplateByElement(element);
        
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
    public BpmUpdater getBpmnUpdater(Block block) {
        return translateFactories.stream()
                .filter(factory -> factory.getBlockType().isPresent() && factory.getBlockType().get().equals(block.getType()))
                .findFirst()
                .flatMap(factory -> factory.createBpmUpdater(block))
                .orElseThrow(() -> new IllegalArgumentException("No Bpmn document updater for this type of block: " + block.getType()));
    }
    
    private TemplateForParser getTemplateByElement(String elementName) {
        return templatesForParser.stream()
            .filter(template -> template.getElement().equals(elementName))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No template found for element: " + elementName));
    }
    
}
