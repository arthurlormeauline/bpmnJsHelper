package com.protectline.bpmninjs.util;

import com.protectline.bpmninjs.engine.files.FileUtil;
import com.protectline.bpmninjs.application.DefaultTranslateUnitFactoryProvider;
import com.protectline.bpmninjs.engine.mainfactory.MainFactory;

import java.io.IOException;

public class MainFactoryTestUtil {
    
    public static MainFactory createWithDefaults(FileUtil fileUtil) throws IOException {
        return new MainFactory(fileUtil, new DefaultTranslateUnitFactoryProvider());
    }
}
