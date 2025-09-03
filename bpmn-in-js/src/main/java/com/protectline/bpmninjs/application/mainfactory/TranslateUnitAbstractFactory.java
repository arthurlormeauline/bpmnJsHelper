package com.protectline.bpmninjs.application.mainfactory;

import com.protectline.bpmninjs.application.tobpmn.blockstobpmn.BpmUpdater;
import com.protectline.bpmninjs.application.tobpmn.jstoblocks.UpdateBlockFromJs;
import com.protectline.bpmninjs.application.tojsproject.bpmntoblocks.BlockBuilder;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.common.block.BlockType;
import com.protectline.bpmninjs.jsproject.blocksfactory.BlockFromElement;
import com.protectline.bpmninjs.translateunit.TemplateForParser;
import com.protectline.bpmninjs.translateunit.JsUpdaterTemplate;

import java.util.List;
import java.util.Optional;

public interface TranslateUnitAbstractFactory {

    /**
     * Returns the element names that this factory can handle (e.g., ["function"] or ["main"])
     * Empty list means this factory handles all elements (fallback)
     */
    List<String> getElementNames();

    /**
     * Creates a BlockBuilder if this factory handles the element
     */
    Optional<BlockBuilder> createBlockBuilder();

    /**
     * Creates a BlockFromElement for the given template and element
     */
    Optional<BlockFromElement> createBlockFromElement(TemplateForParser template, String elementName);

    /**
     * Creates an UpdateBlockFromJs if this factory handles the given type
     */
    Optional<UpdateBlockFromJs> createUpdateBlockFromJs(BlockType type);

    /**
     * Creates a BpmUpdater for BPMN document updates
     */
    Optional<BpmUpdater> createBpmUpdater(Block block);

    /**
     * Returns the BlockType that this factory handles, or empty if it doesn't handle any specific type
     */
    Optional<BlockType> getBlockType();

    /**
     * Returns true if this factory can handle the given element name
     */
    boolean canHandleElement(String elementName);

    /**
     * Returns the JS updater templates provided by this factory for JS code generation
     */
    List<JsUpdaterTemplate> getJsUpdaterTemplates(com.protectline.bpmninjs.files.FileUtil fileUtil);

    /**
     * Returns the templates for parser provided by this factory for block parsing
     */
    List<TemplateForParser> getTemplatesForParser(com.protectline.bpmninjs.files.FileUtil fileUtil);

    /**
     * Creates a JsUpdater for JS code generation if this factory handles the given type
     */
    Optional<com.protectline.bpmninjs.jsproject.JsUpdater> createJsUpdater(BlockType type, JsUpdaterTemplate template);
}
