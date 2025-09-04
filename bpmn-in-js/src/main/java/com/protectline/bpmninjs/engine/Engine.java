package com.protectline.bpmninjs.engine;

import com.protectline.bpmninjs.engine.mainfactory.MainFactory;
import com.protectline.bpmninjs.engine.mainfactory.TranslateUnit;
import com.protectline.bpmninjs.engine.tobpmn.JsProjectToBpmn;
import com.protectline.bpmninjs.engine.tojsproject.BpmnToJsProject;
import com.protectline.bpmninjs.engine.files.FileService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;

public class Engine {

    List<TranslateUnit> translateUnitFactories;

    public Engine(List<TranslateUnit> translateUnitFactories){
        this.translateUnitFactories = translateUnitFactories;
    }

    public void run(String[] args) throws URISyntaxException, IOException {
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

    private void toBpmn(String process) throws IOException {
        System.out.println("Update bpmn file from js project");
        Path workingDirectory = Path.of("data");
        FileService fileService = new FileService(workingDirectory);
        MainFactory mainFactory = new MainFactory(fileService, translateUnitFactories);
        JsProjectToBpmn toBpmn = new JsProjectToBpmn(fileService, mainFactory);
        toBpmn.updateBpmn(process);
    }

    private void toJsProject(String process) throws IOException {
        System.out.println("Create js project from bpmn file");
        Path workingDirectory = Path.of("data");
        FileService fileService = new FileService(workingDirectory);
        var mainFactory = new MainFactory(fileService, translateUnitFactories);
        BpmnToJsProject toJs = new BpmnToJsProject(fileService, mainFactory);
        toJs.createProject(process);
    }
}
