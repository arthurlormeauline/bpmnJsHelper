package com.protectline.jsproject.parser;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TemplateLoader {
    
    private static final String TEMPLATE_FILE = "/tobpmn/jsupdatertemplates/jsupdatertemplates.json";
    private static TemplateLoader instance;
    private Map<String, Template> templatesByElement;
    
    private TemplateLoader() throws IOException {
        loadTemplates();
    }
    
    public static TemplateLoader getInstance() throws IOException {
        if (instance == null) {
            instance = new TemplateLoader();
        }
        return instance;
    }
    
    private void loadTemplates() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = getClass().getResourceAsStream(TEMPLATE_FILE);
        
        if (inputStream == null) {
            throw new IOException("Template file not found: " + TEMPLATE_FILE);
        }
        
        Template[] templates = mapper.readValue(inputStream, Template[].class);
        templatesByElement = Arrays.stream(templates)
                .collect(Collectors.toMap(Template::getElement, template -> template));
    }
    
    public List<Template> getTemplatesInOrder() {
        // Retourne les templates dans l'ordre défini dans le JSON (MAIN puis FUNCTION)
        return Arrays.asList(
                templatesByElement.get("main"),
                templatesByElement.get("function")
        );
    }
    
    public Template getTemplateByElement(String element) {
        return templatesByElement.get(element);
    }
}