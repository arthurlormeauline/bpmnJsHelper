package com.protectline;

import com.ctc.wstx.shaded.msv_core.util.Uri;
import com.google.common.io.Resources;
import com.protectline.bpmndocument.model.BpmnPath;
import com.protectline.bpmndocument.model.NodeType;
import com.protectline.bpmndocument.model.camundaadapter.BpmnCamundaDocument;
import com.protectline.jsproject.model.block.FunctionBlock;
import com.protectline.tobpmn.JsProjectToBpmn;
import com.protectline.tobpmn.bpmnupdate.bpmndocumentupdater.MainBpmnDocumentUpdater;
import com.protectline.tojsproject.BpmnToJS;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws URISyntaxException {
//        if (args.length > 2 || args.length < 1) {
//            System.out.println("usage : javac [app] processName [options : -toJs | -toBpmn]");
//        }
//        if (args.length == 1) {
//            toJsProject(args[0]);
//        } else {
//            String option = args[1];
//            if (option.equals("-toJs")) {
//                toJsProject(args[0]);
//            } else if (option.equals("-toBpmn")) {
//                toBpmn(args[0]);
//            } else {
//                System.out.println("usage : javac [app] [options : -toJs | -toBpmn]");
//            }
//        }
        var testWorkingDirectory = Path.of("tests");

        var expectedDelayDefinition = getExpectedBlock("Activity_0q6wfo8", "Delay_definition_0", "NEW : delay definition script", NodeType.SCRIPT);
        var expectedGetDeviceByName_0 = getExpectedBlock("Activity_0rwauzg", "Get_device_by_name_0", "NEW : get device by name url script", NodeType.SERVICE_TASK);
        var expectedGetDeviceByName_1 = getExpectedBlock("Activity_0rwauzg", "Get_device_by_name_1", "NEW : get device by name output script", NodeType.SERVICE_TASK);
        var expectedStartEvent = getExpectedBlock("Event_1u1d1qi", "Event_1u1d1qi_0", "NEW : start event script", NodeType.START);

        var mainUpdater = new MainBpmnDocumentUpdater(List.of(expectedDelayDefinition, expectedGetDeviceByName_0, expectedGetDeviceByName_1, expectedStartEvent));


        // Given
        BpmnCamundaDocument document = new BpmnCamundaDocument(testWorkingDirectory, "simplify");
        Path bpmnFile = testWorkingDirectory.resolve("input/simplify.bpmn");

        // When
        mainUpdater.updateDocument(document);

        // Then
        var test = bpmnFile.toFile().exists();
        document.writeToFile(bpmnFile.toFile());
    }

    public static FunctionBlock getExpectedBlock(String id, String name, String script, NodeType nodeType) {
        BpmnPath expectedPath = new BpmnPath(id);
        String expectedName = name;
        String expectedContent = script;
        return new FunctionBlock(expectedPath, expectedName, expectedContent, nodeType);
    }
    private static void toBpmn(String process) throws URISyntaxException {
        System.out.println("Update bpmn file from js project");
        Path workingDirectory = Path.of(Resources.getResource("/").toURI());
        JsProjectToBpmn toBpmn = new JsProjectToBpmn(workingDirectory);
        toBpmn.updateBpmn(process);
    }

    private static void toJsProject(String process) throws URISyntaxException {
        System.out.println("Create js project from bpmn file");
        Path workingDirectory = Path.of(Resources.getResource("/").toURI());
        BpmnToJS toJs = new BpmnToJS(workingDirectory);
        toJs.createProject(process);
    }
}
