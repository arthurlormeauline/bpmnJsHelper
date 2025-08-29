package com.protectline.jsproject.parser;

import java.io.IOException;

public class JsParserFactory {
    
    private final TemplateLoader templateLoader;
    
    public JsParserFactory() throws IOException {
        this.templateLoader = TemplateLoader.getInstance();
    }
    
    /**
     * Crée un parser basé sur l'élément fourni
     * @param element l'élément à parser (ex: "main", "function")
     * @return le parser approprié
     */
    public JsParser createParser(String element) {
        Template template = templateLoader.getTemplateByElement(element);
        
        if (template == null) {
            throw new IllegalArgumentException("No template found for element: " + element);
        }
        
        switch (element) {
            case "main":
                return new MainParser(template);
            case "function":
                return new FunctionParser(template);
            default:
                throw new UnsupportedOperationException("Parser not implemented for element: " + element);
        }
    }
}