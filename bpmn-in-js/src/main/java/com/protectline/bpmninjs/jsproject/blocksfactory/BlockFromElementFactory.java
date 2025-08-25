package com.protectline.bpmninjs.jsproject.blocksfactory;

import com.protectline.bpmninjs.files.FileUtil;
import com.protectline.bpmninjs.jsproject.updatertemplate.JsUpdaterTemplateUtil;
import com.protectline.bpmninjs.jsproject.updatertemplate.TemplateForParser;

import java.io.IOException;
import java.util.List;

class BlockFromElementFactory {
    
    private final List<TemplateForParser> templates;
    
    BlockFromElementFactory(FileUtil fileUtil) throws IOException {
        this.templates = JsUpdaterTemplateUtil.readTemplatesForParserFromFile(fileUtil);
    }
    
    BlockFromElementBuilder getBlockBuilder(String element) {
        TemplateForParser template = getTemplateByElement(element);
        
        switch (element) {
            case "main":
                return new MainBlockFromElementBuilder(template);
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
