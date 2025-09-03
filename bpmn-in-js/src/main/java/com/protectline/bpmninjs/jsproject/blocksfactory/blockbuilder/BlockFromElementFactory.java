package com.protectline.bpmninjs.jsproject.blocksfactory.blockbuilder;

import com.protectline.bpmninjs.files.FileUtil;
import com.protectline.bpmninjs.functionfactory.FunctionBlockFromElementBuilder;
import com.protectline.bpmninjs.jsproject.blocksfactory.BlockFromElement;
import com.protectline.bpmninjs.jsproject.updatertemplate.JsUpdaterTemplateUtil;
import com.protectline.bpmninjs.jsproject.updatertemplate.TemplateForParser;
import com.protectline.bpmninjs.application.entrypointfactory.EntryPointBlockFromElement;

import java.io.IOException;
import java.util.List;

public class BlockFromElementFactory {
    
    private final List<TemplateForParser> templates;
    
    public BlockFromElementFactory(FileUtil fileUtil) throws IOException {
        this.templates = JsUpdaterTemplateUtil.readTemplatesForParserFromFile(fileUtil);
    }
    
    public BlockFromElement getBlockBuilder(String element) {
        TemplateForParser template = getTemplateByElement(element);
        
        switch (element) {
            case "main":
                return new EntryPointBlockFromElement(template);
            case "function":
                return new FunctionBlockFromElementBuilder(template);
            default:
                throw new UnsupportedOperationException("Parser not implemented for element: " + element);
        }
    }
    
    private TemplateForParser getTemplateByElement(String elementName) {
        return templates.stream()
            .filter(template -> template.getElement().equals(elementName))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No template found for element: " + elementName));
    }
}
