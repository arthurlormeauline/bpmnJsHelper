package com.protectline.bpmninjs;

import com.protectline.bpmninjs.application.Application;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        String[] arg = new String[]{"tus.prc.actionCombine", "-toBpmn"};
       Application.run(arg);
    }
}
