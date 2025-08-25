package com.protectline;

import com.ctc.wstx.shaded.msv_core.util.Uri;
import com.google.common.io.Resources;
import com.protectline.tobpmn.JsProjectToBpmn;
import com.protectline.tojsproject.BpmnToJS;

import java.net.URISyntaxException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws URISyntaxException {
        if (args.length > 2 || args.length < 1) {
            System.out.println("usage : javac [app] processName [options : -toJs | -toBpmn]");
        }
        if (args.length == 1) {
            toJsProject(args[0]);
        } else {
            String option = args[1];
            if (option.equals("-toJs")) {
                toJsProject(args[0]);
            } else if (option.equals("-toBpmn")) {
                toBpmn(args[0]);
            } else {
                System.out.println("usage : javac [app] [options : -toJs | -toBpmn]");
            }
        }
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
