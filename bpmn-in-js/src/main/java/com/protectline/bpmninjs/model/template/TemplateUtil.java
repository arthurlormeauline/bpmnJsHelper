package com.protectline.bpmninjs.model.template;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class TemplateUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<Template> loadFromFile(Path templateJsonFile) throws IOException {
        List<TemplateJson> jsonTemplates = objectMapper.readValue(
            templateJsonFile.toFile(), 
            new TypeReference<List<TemplateJson>>() {}
        );
        return jsonTemplates.stream()
                .map(TemplateJson::toTemplate)
            .collect(Collectors.toList());
    }
    
}
