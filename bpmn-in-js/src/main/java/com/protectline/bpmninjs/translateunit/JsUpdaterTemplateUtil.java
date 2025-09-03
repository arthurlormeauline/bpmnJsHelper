package com.protectline.bpmninjs.translateunit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.protectline.bpmninjs.files.FileUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class JsUpdaterTemplateUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<JsUpdaterTemplate> readJsUpdaterTemplatesFromFile(FileUtil fileUtil) throws IOException {
        Path templatesFile = fileUtil.getJsUpdaterTemplatesJsonFile();
        List<TemplateJson> jsonTemplates = objectMapper.readValue(
            templatesFile.toFile(), 
            new TypeReference<List<TemplateJson>>() {}
        );
        return jsonTemplates.stream()
            .map(TemplateJson::toJsUpdaterTemplate)
            .collect(Collectors.toList());
    }
    
    public static List<TemplateForParser> readTemplatesForParserFromFile(FileUtil fileUtil) throws IOException {
        Path templatesFile = fileUtil.getJsUpdaterTemplatesJsonFile();
        List<TemplateJson> jsonTemplates = objectMapper.readValue(
            templatesFile.toFile(), 
            new TypeReference<List<TemplateJson>>() {}
        );
        return jsonTemplates.stream()
            .map(TemplateJson::toTemplateForParser)
            .collect(Collectors.toList());
    }

    public static List<JsUpdaterTemplate> readJsUpdaterTemplatesFromFile(FileUtil fileUtil, String subFolder) throws IOException {
        Path templatesFile = fileUtil.getJsUpdaterTemplatesJsonFile().getParent()
                .resolve(subFolder).resolve("jsupdatertemplate.json");
        List<TemplateJson> jsonTemplates = objectMapper.readValue(
            templatesFile.toFile(), 
            new TypeReference<List<TemplateJson>>() {}
        );
        return jsonTemplates.stream()
            .map(TemplateJson::toJsUpdaterTemplate)
            .collect(Collectors.toList());
    }
    
    public static List<TemplateForParser> readTemplatesForParserFromFile(FileUtil fileUtil, String subFolder) throws IOException {
        Path templatesFile = fileUtil.getJsUpdaterTemplatesJsonFile().getParent()
                .resolve(subFolder).resolve("jsupdatertemplate.json");
        List<TemplateJson> jsonTemplates = objectMapper.readValue(
            templatesFile.toFile(), 
            new TypeReference<List<TemplateJson>>() {}
        );
        return jsonTemplates.stream()
            .map(TemplateJson::toTemplateForParser)
            .collect(Collectors.toList());
    }
}
