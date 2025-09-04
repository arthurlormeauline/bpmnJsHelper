package com.protectline.bpmninjs.application;

import com.protectline.bpmninjs.application.function.FunctionTranslateUnitFactory;
import com.protectline.bpmninjs.engine.Engine;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        String[] arg = new String[]{"tus.prc.actionCombine", "-toBpmn"};
        Engine engine = new Engine(List.of(new FunctionTranslateUnitFactory()));
        engine.run(arg);
    }
}
