package com.protectline.bpmninjs.jsprojectimpl;

import com.protectline.bpmninjs.application.mainfactory.MainFactory;
import com.protectline.bpmninjs.application.files.FileUtil;
import com.protectline.bpmninjs.jsproject.JsNode;
import com.protectline.bpmninjs.jsproject.JsProject;
import com.protectline.bpmninjs.xmlparser.XmlParser;
import com.protectline.bpmninjs.xmlparser.JsProjectTokenDefinition;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JsProjectImpl implements JsProject {

    private final FileUtil fileUtil;
    private final MainFactory mainFactory;
    private final String process;
    private final XmlParser xmlParser;

    public JsProjectImpl(String process, FileUtil fileUtil, MainFactory mainFactory) throws IOException {
        this.process = process;
        this.fileUtil = fileUtil;
        this.mainFactory = mainFactory;
        this.xmlParser = new XmlParser();
    }

    @Override
    public List<JsNode> getElements() throws IOException {
        var jsContent = fileUtil.getJsRunnerFileContent(process);
        var elements = xmlParser.parseXml(jsContent, new JsProjectTokenDefinition());
        return elements.stream()
                .map(JsNodeImpl::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getJsContent() throws IOException {
        return fileUtil.getJsRunnerFileContent(process);
    }

    @Override
    public void writeJsContent(String updatedContent) throws IOException {
        java.nio.file.Path jsProjectDirectory = fileUtil.getJsProjectDirectory(process);
        java.nio.file.Path bpmnRunnerFile = jsProjectDirectory.resolve("BpmnRunner.js");
        java.nio.file.Files.writeString(bpmnRunnerFile, updatedContent);
    }
}
