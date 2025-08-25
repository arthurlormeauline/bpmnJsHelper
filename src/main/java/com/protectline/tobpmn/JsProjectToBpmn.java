package com.protectline.tobpmn;

import com.protectline.tobpmn.jsprojectparser.JsProjectParser;
import com.protectline.tojsproject.model.element.Element;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class JsProjectToBpmn {
    private final Path workingDirectory;

    public JsProjectToBpmn(Path workingDirectory){
        this.workingDirectory = workingDirectory;
    }

    public void updateBpmn(String process) {
        BlockSelector blockSelector = new BlockSelector();
        ElementBuilder elementBuilder = new ElementBuilder(blockSelector);
        List<Element> elements = elementBuilder.buildElements();


        ElementSelector elementSelector = new ElementSelector();
        elementSelector.updateBpmn(elements);
    }
}
