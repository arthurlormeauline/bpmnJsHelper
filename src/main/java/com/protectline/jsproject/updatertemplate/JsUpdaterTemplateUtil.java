package com.protectline.jsproject.updatertemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.protectline.files.FileUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class JsUpdaterTemplateUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<JsUpdaterTemplate> readTemplatesFromFile(FileUtil fileUtil) throws IOException {
        Path templatesFile = fileUtil.getJsUpdaterTemplatesJsonFile();
        List<JsUpdaterTemplateJson> jsonTemplates = objectMapper.readValue(
            templatesFile.toFile(), 
            new TypeReference<List<JsUpdaterTemplateJson>>() {}
        );
        return jsonTemplates.stream()
            .map(JsUpdaterTemplateJson::toJsUpdaterTemplate)
            .collect(Collectors.toList());
    }
}
