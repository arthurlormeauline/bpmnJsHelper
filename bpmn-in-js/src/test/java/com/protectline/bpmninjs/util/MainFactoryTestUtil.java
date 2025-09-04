package com.protectline.bpmninjs.util;

import com.protectline.bpmninjs.engine.files.FileService;
import com.protectline.bpmninjs.engine.mainfactory.MainFactory;
import com.protectline.bpmninjs.application.function.FunctionTranslateUnit;

import java.io.IOException;
import java.util.List;

public class MainFactoryTestUtil {
    
    public static MainFactory createWithDefaults(FileService fileService) throws IOException {
        return new MainFactory(fileService, List.of(new FunctionTranslateUnit()));
    }
}
