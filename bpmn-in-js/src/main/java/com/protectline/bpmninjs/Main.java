package com.protectline.bpmninjs;

import com.protectline.bpmninjs.application.Application;
import com.protectline.bpmninjs.application.tobpmn.JsProjectToBpmn;
import com.protectline.bpmninjs.application.tojsproject.BpmnToJS;
import com.protectline.bpmninjs.files.FileUtil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
       Application.run(args);
    }
}
