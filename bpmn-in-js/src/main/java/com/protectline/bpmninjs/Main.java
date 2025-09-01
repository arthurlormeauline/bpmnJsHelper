package com.protectline.bpmninjs;

import com.protectline.bpmninjs.application.tobpmn.JsProjectToBpmn;
import com.protectline.bpmninjs.application.tojsproject.BpmnToJS;
import com.protectline.bpmninjs.files.FileUtil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
//       Application.run(args);

        var testFolderName = "test";
        var fileUtil = new FileUtil(Path.of(testFolderName));
        var bpmnToJs = new BpmnToJS(fileUtil);
        var jsToBpmn = new JsProjectToBpmn(fileUtil);
        var process = "simplify";

//        bpmnToJs.createProject(process);
        jsToBpmn.updateBpmn(process);
    }
}
