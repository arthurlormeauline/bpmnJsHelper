package com.protectline.bpmninjs.util;

import com.protectline.bpmninjs.files.FileUtil;
import com.protectline.bpmninjs.functionfactory.FunctionTranslateUnitFactory;
import com.protectline.bpmninjs.application.entrypointfactory.EntryPointTranslateUnitFactory;
import com.protectline.bpmninjs.application.mainfactory.MainFactory;

import java.io.IOException;

public class MainFactoryTestUtil {
    
    public static MainFactory createWithDefaults(FileUtil fileUtil) throws IOException {
        MainFactory mainFactory = new MainFactory(fileUtil);
        mainFactory.addTranslateFactory(new FunctionTranslateUnitFactory());
        mainFactory.addTranslateFactory(new EntryPointTranslateUnitFactory());
        return mainFactory;
    }
}
