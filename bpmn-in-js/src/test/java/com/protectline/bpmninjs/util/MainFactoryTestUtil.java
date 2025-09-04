package com.protectline.bpmninjs.util;

import com.protectline.bpmninjs.engine.files.FileUtil;
import com.protectline.bpmninjs.engine.mainfactory.MainFactory;
import com.protectline.bpmninjs.application.function.FunctionTranslateUnitFactory;

import java.io.IOException;
import java.util.List;

public class MainFactoryTestUtil {
    
    public static MainFactory createWithDefaults(FileUtil fileUtil) throws IOException {
        return new MainFactory(fileUtil, List.of(new FunctionTranslateUnitFactory()));
    }
}
