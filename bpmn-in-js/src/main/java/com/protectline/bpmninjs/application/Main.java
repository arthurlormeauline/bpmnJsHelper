package com.protectline.bpmninjs.application;

import com.protectline.bpmninjs.application.function.FunctionTranslateUnit;
import com.protectline.bpmninjs.engine.Engine;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        //String[] arg = new String[]{"tus.prc.actionCombine",};
        String[] arg = new String[]{"CreateCustomer_Dev"};
        Engine engine = new Engine(List.of(new FunctionTranslateUnit()));
        engine.run(arg);
    }
}
