package com.protectline;

import com.protectline.application.Application;
import com.protectline.application.tobpmn.JsProjectToBpmn;
import com.protectline.application.tojsproject.BpmnToJS;
import com.protectline.application.tojsproject.bpmntoblocks.FromBpmnToBlocks;
import com.protectline.files.FileUtil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Objects;

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
