package com.protectline.bpmninjs.model.jsproject;

import com.protectline.bpmninjs.engine.mainfactory.MainFactory;
import com.protectline.bpmninjs.engine.files.FileService;
import com.protectline.bpmninjs.model.jsproject.api.JsNode;
import com.protectline.bpmninjs.model.jsproject.api.JsProject;
import com.protectline.bpmninjs.xmlparser.XmlParser;
import com.protectline.bpmninjs.xmlparser.JsProjectTokenDefinition;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JsProjectImpl implements JsProject {

    private final FileService fileService;
    private final MainFactory mainFactory;
    private final String process;
    private final XmlParser xmlParser;

    public JsProjectImpl(String process, FileService fileService, MainFactory mainFactory) throws IOException {
        this.process = process;
        this.fileService = fileService;
        this.mainFactory = mainFactory;
        this.xmlParser = new XmlParser();
    }

    @Override
    public List<JsNode> getElements() throws IOException {
        var jsContent = fileService.getJsRunnerFileContent(process);
        var elements = xmlParser.parseXml(jsContent, new JsProjectTokenDefinition());
        return elements.stream()
                .map(JsNodeImpl::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getJsContent() throws IOException {
        return fileService.getJsRunnerFileContent(process);
    }

    @Override
    public void writeJsContent(String updatedContent) throws IOException {
        java.nio.file.Path jsProjectDirectory = fileService.getJsProjectDirectory(process);
        java.nio.file.Path bpmnRunnerFile = jsProjectDirectory.resolve("BpmnRunner.js");
        java.nio.file.Files.writeString(bpmnRunnerFile, updatedContent);
    }
}
