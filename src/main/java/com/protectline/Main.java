package com.protectline;

import com.protectline.application.Application;
import com.protectline.application.tojsproject.bpmntoblocks.FromBpmnToBlocks;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
//       Application.run(args);

        var testFolderName = "toJsProject";
        var workingdir = Path.of(testFolderName);
        var fromBpmnToBlocks = new FromBpmnToBlocks(workingdir);
        var process = "simplify";

        // When
        fromBpmnToBlocks.createBlocksFromBpmn(process);
    }
}
