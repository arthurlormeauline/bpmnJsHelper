package com.protectline;

import com.protectline.tobpmn.JsProjectToBpmn;
import com.protectline.tojsproject.BpmnToJS;

public class Main {
    public static void main(String[] args) {
        if (args.length > 1) {
            System.out.println("usage : javac [app] [options : -toJs | -toBpmn]");
        }
        if (args.length == 0) {
            toJsProject();
        } else {
            String option = args[0];
            if (option.equals("-toJs")) {
                toJsProject();
            } else if (option.equals("-toBpmn")) {
                toBpmn();
            } else {
                System.out.println("usage : javac [app] [options : -toJs | -toBpmn]");
            }
        }
    }

    private static void toBpmn() {
        System.out.println("Update bpmn file from js project");
        JsProjectToBpmn compiler = new JsProjectToBpmn();
        compiler.updateBpmn();
    }

    private static void toJsProject() {
        System.out.println("Create js project from bpmn file");
        BpmnToJS compiler = new BpmnToJS();
        compiler.createProject();
    }
}
